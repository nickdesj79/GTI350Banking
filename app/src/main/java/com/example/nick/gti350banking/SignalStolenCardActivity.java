package com.example.nick.gti350banking;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignalStolenCardActivity extends AppCompatActivity {

    private OnlineAccount account;
    private ArrayList<CreditCard> cardList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signal_stolen_card);

        account = (OnlineAccount) getIntent().getSerializableExtra("account");

        List<Map<String, String>> notificationList = new ArrayList<Map<String, String>>();
        Spinner cardSpinner = (Spinner)findViewById(R.id.cardSpinner);

        for (CreditCard card : account.getCardList()) {
            if(card.getState().equals("AVAILABLE")) {
                cardList.add(card);
                Map<String, String> cardMap = new HashMap<String, String>(2);
                cardMap.put("cardtype", card.getType());
                cardMap.put("cardnumber", card.getCard_number());
                notificationList.add(cardMap);
            }
        }

        SimpleAdapter adapter = new SimpleAdapter(this, notificationList,
                android.R.layout.simple_list_item_2,
                new String[] {"cardtype", "cardnumber"},
                new int[] {android.R.id.text1,
                        android.R.id.text2});
        cardSpinner.setAdapter(adapter);

    }

    public void confirmStolenCard(View v) {

        Spinner cardSpinner = (Spinner) findViewById(R.id.cardSpinner);
        String stolenCard = cardSpinner.getSelectedItem().toString();

        stolenCard = stolenCard.substring(1, stolenCard.length()-1);
        String[] keyValuePairs = stolenCard.split(",");
        Map<String,String> map = new HashMap<>();

        for(String pair : keyValuePairs)
        {
            String[] entry = pair.split("=");
            map.put(entry[0].trim(), entry[1].trim());
        }

        final String cardNumber = (String) map.get("cardnumber");
        final String cardType = (String) map.get("cardtype");





        LayoutInflater layoutInflater = LayoutInflater.from(SignalStolenCardActivity
                .this);
        final View promptView = layoutInflater.inflate(R.layout.prompt_stolen_card_confirm, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignalStolenCardActivity.this);
        alertDialogBuilder.setView(promptView);


        TextView cardNumberTF = (TextView) promptView.findViewById(R.id.cardNumber);
        TextView cardTypeTF = (TextView) promptView.findViewById(R.id.cardType);

        cardNumberTF.setText(cardNumber);
        cardTypeTF.setText(cardType);


        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        updateCard(cardType, cardNumber);

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

    private void updateCard(String cardType, String cardNumber) {

        CreditCard cardToUpdate = null;

        for(CreditCard card : account.getCardList()) {
            if(card.getType().equals(cardType) && card.getCard_number().equals(cardNumber)) {
                cardToUpdate = card;
            }
        }
        cardToUpdate.setState("STOLEN");

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
