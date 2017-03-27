package com.example.nick.gti350banking;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Nick on 2017-03-15.
 */

public class CreditCard extends Card {

    private String type;
    private ArrayList<CreditCardInvoices> invoicesList = new ArrayList<>();

    private int balances = 0;


    public CreditCard(String card_number, String type) {
        super(card_number);

        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void addInvoices(CreditCardInvoices c) {
        invoicesList.add(c);
    }

    public ArrayList<CreditCardInvoices> getInvoicesList() {
        return invoicesList;
    }

    public void setInvoicesList(ArrayList<CreditCardInvoices> invoicesList) {
        this.invoicesList = invoicesList;
    }

    public int getBalances() {
        return balances;
    }

    public void setBalances(int balances) {
        this.balances = balances;
    }
}


