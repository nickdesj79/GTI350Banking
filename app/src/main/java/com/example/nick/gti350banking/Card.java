package com.example.nick.gti350banking;

import java.io.Serializable;

/**
 * Created by Nick on 2017-03-15.
 */

public class Card implements Serializable {

    private String card_number;

    public Card(String card_number) {
        this.card_number = card_number;
    }

}
