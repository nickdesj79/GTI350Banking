package com.example.nick.gti350banking;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Nick on 2017-03-15.
 */

public class OnlineAccount implements Serializable {

    //used as account name
    private String email;

    //wont be encrypted for school work usage
    private String password;

    private ArrayList<CreditCard> cardList = new ArrayList<>();

    private SavingsAccount savingAccount;

    private CheckingsAccount chekingAccount;

    public OnlineAccount(String email, String password) {
        this.email = email;
        this.password = password;

    }

    public void addCard(CreditCard c) {
        cardList.add(c);
    }

    public ArrayList<CreditCard> getCardList() {
        return this.cardList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SavingsAccount getSavingAccount() {
        return savingAccount;
    }

    public void setSavingAccount(SavingsAccount savingAccount) {
        this.savingAccount = savingAccount;
    }

    public CheckingsAccount getChekingAccount() {
        return chekingAccount;
    }

    public void setChekingAccount(CheckingsAccount chekingAccount) {
        this.chekingAccount = chekingAccount;
    }
}
