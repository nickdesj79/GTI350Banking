package com.example.nick.gti350banking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainMenuActivity extends AppCompatActivity {

    private OnlineAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        account = (OnlineAccount) getIntent().getSerializableExtra("account");

        TextView chekingTF = (TextView) findViewById(R.id.cheking);
        TextView savingTF = (TextView) findViewById(R.id.saving);

        chekingTF.setText(account.getChekingAccount().getAmount() + "$");
        savingTF.setText(account.getSavingAccount().getAmount() + "$");
    }

    public void openMenu(View v) {


        Intent i = new Intent(getApplicationContext(), RightMenuActivity.class);

        i.putExtra("account", account);
        startActivity(i);
        finish();
    }
}
