package com.example.nick.gti350banking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class ConnexionScreenActivity extends AppCompatActivity {

    ArrayList<OnlineAccount> accountList = new ArrayList<OnlineAccount>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion_screen);

        /*sur la création, on crée 2 comptes fictifs qui seront utilisés pour la démo
        ces comptes sont crées seulement si le Singleton qui nous sert de serveur était initialement nul.
        sinon il ne les crée pas.*/

        if(SingletonAccountManager.getInstance() == null) {
            SingletonAccountManager.createInstance();

            //setting les informations du comptes #1 sur le download de l'application
            OnlineAccount account1 = new OnlineAccount("abcde@hotmail.com", "abcde");
            CheckingsAccount chkgsAcc1 = new CheckingsAccount(1000);
            SavingsAccount svgsAcc1 = new SavingsAccount(5000);
            account1.setChekingAccount(chkgsAcc1);
            account1.setSavingAccount(svgsAcc1);
            CreditCard card1 = new CreditCard("1234567898765432","MasterCard");
            account1.addCard(card1);

            CreditCardInvoices invoices1 = new CreditCardInvoices("Kanda", 20);
            CreditCardInvoices invoices2 = new CreditCardInvoices("3Dragons", 77);

            card1.setBalances(97);

            card1.addInvoices(invoices1);
            card1.addInvoices(invoices2);

            //setting les informations du comptes #2 sur le download de l'application
            OnlineAccount account2 = new OnlineAccount("12345@hotmail.com", "12345");
            CheckingsAccount chkgsAcc2 = new CheckingsAccount(2000);
            SavingsAccount svgsAcc2 = new SavingsAccount(3000);
            account2.setChekingAccount(chkgsAcc2);
            account2.setSavingAccount(svgsAcc2);

            SingletonAccountManager.getInstance().addAccount(account1);
            SingletonAccountManager.getInstance().addAccount(account2);


        }
        accountList = SingletonAccountManager.getInstance().getAccountList();

    }

    public void connect(View v) {

        boolean accountExist = false;

        EditText etEmail = (EditText) findViewById(R.id.email);
        String email = etEmail.getText().toString();

        EditText etPassword = (EditText) findViewById(R.id.password);
        String password = etPassword.getText().toString();


        for(OnlineAccount a : accountList) {
            if(a.getEmail().equals(email)) {
                if(a.getPassword().equals(password)) {
                    goToHomePage(a);
                    accountExist = true;

                }
            }
        }


        if(!accountExist) {
            performLoginError();
        }
    }

    //redigirer l'app vers le menu principal
    private void goToHomePage(OnlineAccount a) {

        Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);

        i.putExtra("account", a);
        startActivity(i);
        finish();
    }

    //TODO
    //Changer la page pour montrer qu'une erreur s'est produite
    private void performLoginError() {

    }
}
