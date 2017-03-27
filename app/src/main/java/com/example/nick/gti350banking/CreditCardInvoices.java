package com.example.nick.gti350banking;

import java.io.Serializable;

/**
 * Created by Nick on 2017-03-27.
 */

public class CreditCardInvoices implements Serializable{

    private String company;
    private int amount;

    public CreditCardInvoices(String company, int amount) {
        this.company = company;
        this.amount = amount;
    }

    public String getCompany() {
        return company;
    }

    public int getAmount() {
        return amount;
    }

}
