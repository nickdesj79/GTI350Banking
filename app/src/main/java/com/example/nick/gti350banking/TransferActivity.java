package com.example.nick.gti350banking;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class TransferActivity extends AppCompatActivity {

    private OnlineAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        account = (OnlineAccount) getIntent().getSerializableExtra("account");

        //initialiser la liste qui s'en va dans les dropdown list
        String[]list = {"Checkings       " + account.getChekingAccount().getAmount()+"$", "Savings       " + account.getSavingAccount().getAmount()+"$"};

        //set le display de la liste de from account pour le transfert
        Spinner fromAcc = (Spinner)findViewById(R.id.fromAccount);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TransferActivity.this,
                android.R.layout.simple_spinner_item,list);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromAcc.setAdapter(adapter);

        String[]list2 = {"Savings       " + account.getSavingAccount().getAmount()+"$", "Checkings       " + account.getChekingAccount().getAmount()+"$"};
        //set le display de  la liste de to account pour le transfert
        Spinner toAcc = (Spinner)findViewById(R.id.toAccount);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(TransferActivity.this,
                android.R.layout.simple_spinner_item,list2);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toAcc.setAdapter(adapter);


    }

    public void confirmTransfer(View v) {
        Spinner fromAccountSpinner = (Spinner) findViewById(R.id.fromAccount);
        String fromAccount = fromAccountSpinner.getSelectedItem().toString();

        Spinner toAccountSpinner = (Spinner) findViewById(R.id.toAccount);
        String toAccount = toAccountSpinner.getSelectedItem().toString();

        //si les deux montants proviennent du même compte
        if(fromAccount.substring(0,5).equals(toAccount.substring(0,5))) {
            displayError("SAME_ACCOUNT");
        } else {
            EditText amountET = (EditText) findViewById(R.id.amount);
            int amount = Integer.parseInt(amountET.getText().toString());

            if(fromAccount.startsWith("Check")) {
                if(account.getChekingAccount().getAmount() < amount) {
                    displayError("NOT_ENOUGH_MONEY");
                } else {
                    proceed("Checking","Saving",amount);
                }
            } else {
                if(account.getSavingAccount().getAmount() < amount) {
                    displayError("NOT_ENOUGH_MONEY");
                } else {
                    proceed("Saving","Checking",amount);
                }
            }
        }
    }

    //display un pop up de confirmation et attend une réponse de l'utilisateur s'il désire confirmer son transfer.
    private void proceed(final String fromAccount, final String toAccount, final int amount) {
        LayoutInflater layoutInflater = LayoutInflater.from(TransferActivity.this);
        final View promptView = layoutInflater.inflate(R.layout.prompt_transfer_confirmation, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TransferActivity.this);
        alertDialogBuilder.setView(promptView);

        TextView amountTF = (TextView) promptView.findViewById(R.id.amount);
        TextView fromTF = (TextView) promptView.findViewById(R.id.from);
        TextView toTF = (TextView) promptView.findViewById(R.id.to);
        TextView checkingbalanceTF = (TextView) promptView.findViewById(R.id.chekingBalance);
        TextView savingBalanceTF = (TextView) promptView.findViewById(R.id.savingBalance);


        amountTF.setText(amount + "$");
        fromTF.setText(fromAccount);
        toTF.setText(toAccount);

        if(fromAccount.startsWith("Check")) {
            checkingbalanceTF.setText((account.getChekingAccount().getAmount()-amount) + "$");
            savingBalanceTF.setText((account.getSavingAccount().getAmount()+amount) + "$");
        } else {
            checkingbalanceTF.setText((account.getChekingAccount().getAmount()+amount) + "$");
            savingBalanceTF.setText((account.getSavingAccount().getAmount()-amount) + "$");
        }

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        updateAccountBalances(fromAccount,toAccount,amount);

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

    private void updateAccountBalances(String fromAccount, String toAccount,int amount) {

        if(fromAccount.startsWith("Checking")) {
            account.getChekingAccount().setAmount(account.getChekingAccount().getAmount()-amount);
            account.getSavingAccount().setAmount(account.getSavingAccount().getAmount()+amount);
        } else {
            account.getChekingAccount().setAmount(account.getChekingAccount().getAmount()+amount);
            account.getSavingAccount().setAmount(account.getSavingAccount().getAmount()-amount);
        }
        Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);

        i.putExtra("account", account);
        startActivity(i);
        finish();

    }

    //TODO
    private void displayError(String errorType) {
        LayoutInflater layoutInflater = LayoutInflater.from(TransferActivity.this);
        final View promptView = layoutInflater.inflate(R.layout.prompt_transfer_error, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TransferActivity.this);
        alertDialogBuilder.setView(promptView);

        TextView amountTF = (TextView) promptView.findViewById(R.id.errorMessage);

        if(errorType.equals("NOT_ENOUGH_MONEY")){
            amountTF.setText("You do not have this amount of money to transfer from your account.");
        } else if (errorType.equals("SAME_ACCOUNT")){
            amountTF.setText("You cannot transfer money onto the same account.");
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
