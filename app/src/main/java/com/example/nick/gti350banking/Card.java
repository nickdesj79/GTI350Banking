package com.example.nick.gti350banking;

import java.io.Serializable;

/**
 * Created by Nick on 2017-03-15.
 */

public class Card implements Serializable {

    private String card_number;
    private String state = "AVAILABLE";

    public Card(String card_number) {
        this.card_number = card_number;
    }


    public String getCard_number() {
        return card_number;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
