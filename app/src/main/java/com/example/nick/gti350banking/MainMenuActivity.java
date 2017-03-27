package com.example.nick.gti350banking;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainMenuActivity extends AppCompatActivity {

    private OnlineAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        account = (OnlineAccount) getIntent().getSerializableExtra("account");

        Button notificationBtn = (Button)findViewById(R.id.notificationButton);
        int notificationAmount = 0;

        for (InteracTransfer it : SingletonAccountManager.getInstance().getInteracTransferList()) {
            if(it.toAccount.getEmail().equals(account.getEmail()) && it.getState().equals("SEND")) {
                notificationAmount++;
            }
        }
        notificationBtn.setText(""+notificationAmount);

        TextView chekingTF = (TextView) findViewById(R.id.cheking);
        TextView savingTF = (TextView) findViewById(R.id.saving);

        TextView creditCardTF = (TextView) findViewById(R.id.creditCardType);
        TextView creditCardamount = (TextView) findViewById(R.id.creditCardAmount);

        if(!account.getCardList().isEmpty()) {
            if(account.getCardList().get(0).getBalances() < 0) {
                creditCardamount.setText(Math.abs(account.getCardList().get(0).getBalances())+ "$ CR");
            } else {
                creditCardamount.setText(account.getCardList().get(0).getBalances() + "$");
            }


            creditCardTF.setText(account.getCardList().get(0).getType());
        }

        chekingTF.setText(account.getChekingAccount().getAmount() + "$");
        savingTF.setText(account.getSavingAccount().getAmount() + "$");
    }


    public void openMenu(View v) {


        Intent i = new Intent(getApplicationContext(), RightMenuActivity.class);

        i.putExtra("account", account);
        startActivity(i);
        finish();
    }

    public void showNotifications(View v) {

        Button btn = (Button)findViewById(R.id.notificationButton);
        String notificationAmount = btn.getText().toString();

        if(Integer.parseInt(notificationAmount) == 0) {
            displayError("NO_NOTIFICATIONS");
        } else {
            Intent i = new Intent(getApplicationContext(), NotificationsActivity.class);

            i.putExtra("account", account);
            startActivity(i);
            finish();
        }
    }

    private void displayError(String errorType) {
        LayoutInflater layoutInflater = LayoutInflater.from(MainMenuActivity.this);
        final View promptView = layoutInflater.inflate(R.layout.prompt_error, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainMenuActivity.this);
        alertDialogBuilder.setView(promptView);

        TextView amountTF = (TextView) promptView.findViewById(R.id.errorMessage);

        if(errorType.equals("NO_NOTIFICATIONS")){
            amountTF.setText("You have no notifications.");
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
