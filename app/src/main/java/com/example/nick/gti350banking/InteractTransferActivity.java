package com.example.nick.gti350banking;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class InteractTransferActivity extends AppCompatActivity {


    private OnlineAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interact_transfer);

        account = (OnlineAccount) getIntent().getSerializableExtra("account");

        //initialiser la liste qui s'en va dans les dropdown list
        String[]list = {"Checkings       " + account.getChekingAccount().getAmount()+"$", "Savings       " + account.getSavingAccount().getAmount()+"$"};

        //set le display de la liste de from account pour le transfert
        Spinner fromAcc = (Spinner)findViewById(R.id.fromAccount);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(InteractTransferActivity.this,
                android.R.layout.simple_spinner_item,list);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromAcc.setAdapter(adapter);
    }

    //analyser les edit text pour voir si les champs ont été remplis.
    public void confirmTransfer(View v) {

        EditText toET = (EditText) findViewById(R.id.to);
        String toAddress = toET.getText().toString();

        EditText amountET = (EditText) findViewById(R.id.amount);
        String amount = amountET.getText().toString();

        EditText passwordET = (EditText) findViewById(R.id.password);
        String password = passwordET.getText().toString();

        Spinner fromAccountSpinner = (Spinner) findViewById(R.id.fromAccount);
        String fromAccount = fromAccountSpinner.getSelectedItem().toString();

        if(toAddress.isEmpty()) {
            displayError("NO_ADDRESS");
        } else if(amount.isEmpty()) {
            displayError("NO_AMOUNT");
        } else if(Integer.parseInt(amount) <= 0) {
            displayError("NEGATIVE_AMOUNT");
        } else if(password.isEmpty()) {
            displayError("NO_PASSWORD");
        }   else {
            OnlineAccount accountToSend = null;
            for(OnlineAccount a : SingletonAccountManager.getInstance().getAccountList()) {
                if(a.getEmail().equals(toAddress)) {
                    accountToSend = a;
                }
            }

            if(accountToSend == null) {
                displayError("ACCOUNT_NO_EXIST");
            } else {
                proceedTransaction(accountToSend,amount, fromAccount, password);
            }
        }


    }

    private void proceedTransaction(final OnlineAccount accountToSend, final String amount, final String fromAccount, final String password) {
        LayoutInflater layoutInflater = LayoutInflater.from(InteractTransferActivity.this);
        final View promptView = layoutInflater.inflate(R.layout.prompt_interact_transfer_confirmation, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(InteractTransferActivity.this);
        alertDialogBuilder.setView(promptView);

        TextView amountTF = (TextView) promptView.findViewById(R.id.amount);
        TextView fromTF = (TextView) promptView.findViewById(R.id.from);
        TextView toTF = (TextView) promptView.findViewById(R.id.to);
        TextView accountBalanceTF = (TextView) promptView.findViewById(R.id.balancesAccount);
        TextView accountAmountTF = (TextView) promptView.findViewById(R.id.accountBalance);


        amountTF.setText(amount + "$");
        toTF.setText(accountToSend.getEmail());

        if(fromAccount.startsWith("Checking")) {
            accountBalanceTF.setText("Checkings");
            fromTF.setText("Checkings");
            accountAmountTF.setText(""+(account.getChekingAccount().getAmount()-Integer.parseInt(amount)) + "$");
        } else {
            accountBalanceTF.setText("Savings");
            fromTF.setText("Savings");
            accountAmountTF.setText(""+(account.getSavingAccount().getAmount()-Integer.parseInt(amount))+ "$");
        }



        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        updateAccountBalances(accountToSend,amount,fromAccount,password);

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void updateAccountBalances(OnlineAccount accountToSend, String amount, String fromAccount, String password) {

        InteracTransfer iTransfer = new InteracTransfer(accountToSend, (float)(Integer.parseInt(amount)), fromAccount, password);
        SingletonAccountManager.getInstance().addInteractTransfer(iTransfer);

        if(fromAccount.startsWith("Checking")) {
            account.getChekingAccount().setAmount(account.getChekingAccount().getAmount()-Integer.parseInt(amount));
        } else {
            account.getSavingAccount().setAmount(account.getSavingAccount().getAmount()-Integer.parseInt(amount));
        }

        OnlineAccount accountToRemove = null;
        //change directly the value of the account in the server
        for(OnlineAccount a : SingletonAccountManager.getInstance().getAccountList()) {
            if(a.getEmail().equals(account.getEmail())) {
                accountToRemove = a;
            }
        }
        SingletonAccountManager.getInstance().getAccountList().remove(accountToRemove);
        SingletonAccountManager.getInstance().addAccount(account);

        Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);

        i.putExtra("account", account);
        startActivity(i);
        finish();

    }


    private void displayError(String errorType) {
        LayoutInflater layoutInflater = LayoutInflater.from(InteractTransferActivity.this);
        final View promptView = layoutInflater.inflate(R.layout.prompt_error, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(InteractTransferActivity.this);
        alertDialogBuilder.setView(promptView);

        TextView amountTF = (TextView) promptView.findViewById(R.id.errorMessage);

        if(errorType.equals("NO_ADDRESS")){
            amountTF.setText("You have to specify an address for your recipient.");
        } else if (errorType.equals("NO_AMOUNT")){
            amountTF.setText("You have to specify an amount of money.");
        } else if (errorType.equals("NEGATIVE_AMOUNT")){
            amountTF.setText("You cannot send less than 0 dollars.");
        }else if (errorType.equals("ACCOUNT_NO_EXIST")){
            amountTF.setText("The account you're trying to send money does not exist.");
        } else if (errorType.equals("NO_PASSWORD")){
            amountTF.setText("You have to specify a security password.");
        }

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }
                });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void openMenu(View v) {


        Intent i = new Intent(getApplicationContext(), RightMenuActivity.class);

        i.putExtra("account", account);
        startActivity(i);
        finish();
    }

    public void goHome(View v) {
        Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);

        i.putExtra("account", account);
        startActivity(i);
        finish();
    }
}
