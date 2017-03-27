package com.example.nick.gti350banking;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MakePaymentActivity extends AppCompatActivity {

    private OnlineAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);

        account = (OnlineAccount) getIntent().getSerializableExtra("account");

        TextView minimumdue = (TextView) findViewById(R.id.minimumdue);

        if(account.getCardList().get(0).getBalances() <= 0) {
            minimumdue.setText(0+"$");
        } else {
            minimumdue.setText(account.getCardList().get(0).getBalances() + "$");
        }

        String[]list = {"Checkings       " + account.getChekingAccount().getAmount()+"$", "Savings       " + account.getSavingAccount().getAmount()+"$"};

        //set le display de la liste de from account pour le transfert
        Spinner fromAcc = (Spinner)findViewById(R.id.fromAccount);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MakePaymentActivity.this,
                android.R.layout.simple_spinner_item,list);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromAcc.setAdapter(adapter);

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

    public void confirmPayment(View v) {
        TextView amountTF = (TextView) findViewById(R.id.amount);
        String amount =amountTF.getText().toString();

        Spinner fromAccountSpinner = (Spinner) findViewById(R.id.fromAccount);
        String fromAccount = fromAccountSpinner.getSelectedItem().toString();



        if(amount.isEmpty()) {
            performError("NO_AMOUNT");
        } else {
            if(fromAccount.startsWith("Checking")) {
                if(account.getChekingAccount().getAmount() < Integer.parseInt(amount)) {
                    performError("NOT_ENOUGH_MONEY");
                } else {
                    proceedPayment("Checking", Integer.parseInt(amount));
                }
            } else {
                if(account.getSavingAccount().getAmount() < Integer.parseInt(amount)) {
                    performError("NOT_ENOUGH_MONEY");
                } else {
                    proceedPayment("Saving", Integer.parseInt(amount));
                }
            }
        }

    }

    private void proceedPayment(final String fromAccount, final int amount) {
        LayoutInflater layoutInflater = LayoutInflater.from(MakePaymentActivity.this);
        final View promptView = layoutInflater.inflate(R.layout.prompt_make_payment_confirmation, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MakePaymentActivity.this);
        alertDialogBuilder.setView(promptView);

        final TextView amountTF = (TextView) promptView.findViewById(R.id.amount);
        TextView fromTF = (TextView) promptView.findViewById(R.id.from);
        TextView toTF = (TextView) promptView.findViewById(R.id.to);
        TextView checkingbalanceTF = (TextView) promptView.findViewById(R.id.chekingBalance);
        TextView savingBalanceTF = (TextView) promptView.findViewById(R.id.savingBalance);


        amountTF.setText(amount + "$");
        fromTF.setText(fromAccount);
        toTF.setText(account.getCardList().get(0).getType());

        if(fromAccount.startsWith("Check")) {
            checkingbalanceTF.setText((account.getChekingAccount().getAmount()-amount) + "$");
            savingBalanceTF.setText(account.getSavingAccount().getAmount() + "$");
        } else {
            checkingbalanceTF.setText(account.getChekingAccount().getAmount() + "$");
            savingBalanceTF.setText((account.getSavingAccount().getAmount()-amount) + "$");
        }

        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    updateAccountBalances(fromAccount,amount);

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

    private void updateAccountBalances(String fromAccount, int amount) {

        if(fromAccount.startsWith("Checking")) {
            account.getChekingAccount().setAmount(account.getChekingAccount().getAmount()-amount);

        } else {
            account.getSavingAccount().setAmount(account.getSavingAccount().getAmount()-amount);
        }

        account.getCardList().get(0).setBalances(account.getCardList().get(0).getBalances()-amount);

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

    private void performError(String errorType) {
        LayoutInflater layoutInflater = LayoutInflater.from(MakePaymentActivity.this);
        final View promptView = layoutInflater.inflate(R.layout.prompt_error, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MakePaymentActivity.this);
        alertDialogBuilder.setView(promptView);

        TextView amountTF = (TextView) promptView.findViewById(R.id.errorMessage);

        if(errorType.equals("NOT_ENOUGH_MONEY")){
            amountTF.setText("You do not have this amount of money to transfer from your account.");
        } else if (errorType.equals("NO_AMOUNT")){
            amountTF.setText("You have to enter a value for the amount of money you want to pay.");
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
}
