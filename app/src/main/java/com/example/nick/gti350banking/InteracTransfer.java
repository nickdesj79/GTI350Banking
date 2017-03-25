package com.example.nick.gti350banking;

import java.io.Serializable;

/**
 * Created by Nick on 2017-03-25.
 */

public class InteracTransfer implements Serializable {

    OnlineAccount toAccount;
    float amount;
    String fromEmail;
    int attempted_try = 3;
    String password;
    String state = "SEND";

    public InteracTransfer(OnlineAccount toAccount, float amount, String fromEmail, String password) {
        this.toAccount = toAccount;
        this.amount = amount;
        this.fromEmail = fromEmail;
        this.password = password;
    }

    public int getAttempted_try() {
        return attempted_try;
    }

    public void setAttempted_try(int attempted_try) {
        this.attempted_try = attempted_try;
    }

    public OnlineAccount getToAccount() {
        return toAccount;
    }

    public float getAmount() {
        return amount;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public String getPassword() {
        return password;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
