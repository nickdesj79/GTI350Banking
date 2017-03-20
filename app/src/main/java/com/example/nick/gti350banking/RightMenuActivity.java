package com.example.nick.gti350banking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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

    //TODO
    public void interacTransfer(View v) {

    }

    public void transfer(View v) {
        Intent i = new Intent(getApplicationContext(), TransferActivity.class);

        i.putExtra("account", account);
        startActivity(i);
        finish();
    }

    //TODO
    public void schedule(View v) {

    }

    //TODO
    public void signalStolenCard(View v) {

    }

    //TODO
    public void makeAPayment(View v) {

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


}
