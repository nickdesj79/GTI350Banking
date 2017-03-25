package com.example.nick.gti350banking;

import java.util.ArrayList;

/**
 * Created by Nick on 2017-03-19.
 */

public class SingletonAccountManager {

    private ArrayList<OnlineAccount> accountList = new ArrayList<OnlineAccount>();
    private ArrayList<InteracTransfer> interacTransferList = new ArrayList<>();

    private static SingletonAccountManager INSTANCE = null;

    private SingletonAccountManager() {

    }

    public static SingletonAccountManager getInstance() {

        return INSTANCE;
    }

    public static void createInstance() {
        INSTANCE = new SingletonAccountManager();
    }

    public void addAccount(OnlineAccount account) {
        accountList.add(account);
    }

    public ArrayList<OnlineAccount> getAccountList() {
        return this.accountList;
    }

    public void addInteractTransfer(InteracTransfer iTransfer) {
        interacTransferList.add(iTransfer);
    }

    public ArrayList<InteracTransfer> getInteracTransferList() {
        return this.interacTransferList;
    }


}
