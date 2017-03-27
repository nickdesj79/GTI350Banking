package com.example.nick.gti350banking;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class RightMenuActivity extends AppCompatActivity {

    private OnlineAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_right_menu);

        account = (OnlineAccount) getIntent().getSerializableExtra("account");

    }

    public void goHome(View v) {
        Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);

        i.putExtra("account", account);
        startActivity(i);
        finish();
    }

    public void interacTransfer(View v) {
        Intent i = new Intent(getApplicationContext(), InteractTransferActivity.class);

        i.putExtra("account", account);
        startActivity(i);
        finish();
    }

    public void transfer(View v) {
        Intent i = new Intent(getApplicationContext(), TransferActivity.class);

        i.putExtra("account", account);
        startActivity(i);
        finish();
    }

    //TODO
    //on redirect vers la page qui présente le display pour céduler un rendez-vous.
    public void schedule(View v) {

        Intent i = new Intent(getApplicationContext(), SchedualAppointmentActivity.class);

        i.putExtra("account", account);
        startActivity(i);
        finish();
    }

    //TODO
    public void signalStolenCard(View v) {

    }

    //TODO
    public void makeAPayment(View v) {

        if(!account.getCardList().isEmpty()){
            Intent i = new Intent(getApplicationContext(), MakePaymentActivity.class);

            i.putExtra("account", account);
            startActivity(i);
            finish();
        }else {
            performError("NO_CARD_REGISTERED");
        }
    }

    public void disconnect(View v) {
        Intent i = new Intent(getApplicationContext(), ConnexionScreenActivity.class);
        startActivity(i);
        finish();
    }

    public void closeMenu(View v) {
        Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);
        i.putExtra("account", account);
        startActivity(i);
        finish();
    }


    private void performError(String errorType) {
        LayoutInflater layoutInflater = LayoutInflater.from(RightMenuActivity.this);
        final View promptView = layoutInflater.inflate(R.layout.prompt_error, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RightMenuActivity.this);
        alertDialogBuilder.setView(promptView);

        TextView amountTF = (TextView) promptView.findViewById(R.id.errorMessage);

       if (errorType.equals("NO_CARD_REGISTERED")){
            amountTF.setText("You have no card registered, therefore, nothing to pay.");
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
