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

public class AcceptingInteractTransferActivity extends AppCompatActivity {

    private OnlineAccount account;
    private InteracTransfer interacTransfer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepting_interact_transfer);

        account = (OnlineAccount) getIntent().getSerializableExtra("account");
        interacTransfer = (InteracTransfer) getIntent().getSerializableExtra("interacTransfer");


        String[]list = {"Checkings       " + account.getChekingAccount().getAmount()+"$", "Savings       " + account.getSavingAccount().getAmount()+"$"};
        //set le display de la liste de from account pour le transfert
        Spinner fromAcc = (Spinner)findViewById(R.id.depositAccount);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AcceptingInteractTransferActivity.this,
                android.R.layout.simple_spinner_item,list);

        fromAcc.setAdapter(adapter);

        TextView from = (TextView) findViewById(R.id.from);
        from.setText(interacTransfer.getFromEmail());

        TextView amount = (TextView) findViewById(R.id.amount);
        amount.setText(interacTransfer.getAmount()+"$");

    }

    public void confirmTransfer(View v) {

        EditText passwordET = (EditText) findViewById(R.id.password);
        String password = passwordET.getText().toString();

        Spinner accountDepositSpinner = (Spinner) findViewById(R.id.depositAccount);
        String depositAccount = accountDepositSpinner.getSelectedItem().toString();

        if(!password.equals(interacTransfer.getPassword())) {
            interacTransfer.setAttempted_try(interacTransfer.getAttempted_try()-1);
            updateTransferDatabase();
            if(interacTransfer.getAttempted_try() == 0) {
                refundInteracTransferSender();
                interacTransfer.setState("FAILED");
                displayError("NO_MORE_TRY");
            } else {
                displayError("WRONG_PASSWORD");
            }
        } else {
            proceedTransfer(depositAccount);
        }

    }

    private void refundInteracTransferSender() {
        OnlineAccount accountToRemove = null;
        //change directly the value of the account in the server
        for(OnlineAccount a : SingletonAccountManager.getInstance().getAccountList()) {
            if(a.getEmail().equals(interacTransfer.getFromEmail())) {
                accountToRemove = a;
            }
        }

        OnlineAccount newAccount = accountToRemove;


        newAccount.getChekingAccount().setAmount(newAccount.getChekingAccount().getAmount() + (int)interacTransfer.getAmount());

        SingletonAccountManager.getInstance().getAccountList().remove(accountToRemove);
        SingletonAccountManager.getInstance().addAccount(newAccount);

    }


    private void proceedTransfer(String depositAccount) {

        interacTransfer.setState("ACCEPTED");

        if(depositAccount.startsWith("Checking")){
            account.getChekingAccount().setAmount(account.getChekingAccount().getAmount()+ (int)interacTransfer.getAmount());
        } else {
            account.getSavingAccount().setAmount(account.getSavingAccount().getAmount()+ (int)interacTransfer.getAmount());
        }

        updateTransferDatabase();



        OnlineAccount accountToRemove = null;
        //change directly the value of the account in the server
        for(OnlineAccount a : SingletonAccountManager.getInstance().getAccountList()) {
            if(a.getEmail().equals(account.getEmail())) {
                accountToRemove = a;
            }
        }
        SingletonAccountManager.getInstance().getAccountList().remove(accountToRemove);
        SingletonAccountManager.getInstance().addAccount(account);

        displayConfirmation(depositAccount);
    }

    private void updateTransferDatabase() {
        InteracTransfer interactTransferToRemove = null;
        //change directly the value of the I.T in the server
        for(InteracTransfer it : SingletonAccountManager.getInstance().getInteracTransferList()) {
            if(it.getToAccount().getEmail().equals(account.getEmail())) {
                interactTransferToRemove = it;
            }
        }
        SingletonAccountManager.getInstance().getInteracTransferList().remove(interactTransferToRemove);
        SingletonAccountManager.getInstance().addInteractTransfer(interacTransfer);
    }

    private void displayConfirmation(String depositAccount) {
        LayoutInflater layoutInflater = LayoutInflater.from(AcceptingInteractTransferActivity.this);
        final View promptView = layoutInflater.inflate(R.layout.prompt_accepting_interact_transfer_succeeded, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AcceptingInteractTransferActivity.this);
        alertDialogBuilder.setView(promptView);

        TextView amountTF = (TextView) promptView.findViewById(R.id.amount);
        TextView fromTF = (TextView) promptView.findViewById(R.id.from);
        TextView accountDepositTF = (TextView) promptView.findViewById(R.id.accountDeposit);
        TextView accountBalanceTF = (TextView) promptView.findViewById(R.id.balancesAccount);
        TextView accountAmountTF = (TextView) promptView.findViewById(R.id.accountBalance);


        amountTF.setText(interacTransfer.getAmount() + "$");
        fromTF.setText(interacTransfer.getFromEmail());

        if(depositAccount.startsWith("Checking")) {
            accountBalanceTF.setText("Checkings");
            accountAmountTF.setText(""+(account.getChekingAccount().getAmount()-(int) interacTransfer.getAmount()) + "$");
        } else {
            accountBalanceTF.setText("Savings");
            accountAmountTF.setText(""+(account.getSavingAccount().getAmount()-(int) interacTransfer.getAmount())+ "$");
        }

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);

                        i.putExtra("account", account);
                        startActivity(i);
                        finish();

                    }
                });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void displayError(final String errorType) {

        LayoutInflater layoutInflater = LayoutInflater.from(AcceptingInteractTransferActivity.this);
        final View promptView = layoutInflater.inflate(R.layout.prompt_error, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AcceptingInteractTransferActivity.this);
        alertDialogBuilder.setView(promptView);

        TextView amountTF = (TextView) promptView.findViewById(R.id.errorMessage);

        if(errorType.equals("WRONG_PASSWORD")) {
            amountTF.setText("You have not provided the good password. You have " + interacTransfer.getAttempted_try() + " left. Then you won't be able to accept the received payment.");
        } else if (errorType.equals("NO_MORE_TRY")) {
            amountTF.setText("You have no try left. The transfer has been cancelled.");
        }

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(errorType.equals("NO_MORE_TRY")) {
                            Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);

                            i.putExtra("account", account);
                            startActivity(i);
                            finish();
                        }

                    }
                });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void goHome(View v) {
        Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);

        i.putExtra("account", account);
        startActivity(i);
        finish();
    }
}
