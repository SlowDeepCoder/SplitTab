package com.example.splittab.FirebaseTemplates;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Participant {
    private String userUID, userName;
    private int amountPayed;
    private ArrayList<Credit> creditList = new ArrayList<>();

    public Participant(String userUID, String userName, int amountPayed, ArrayList<Credit> creditList) {
        this.userUID = userUID;
        this.userName = userName;
        this.amountPayed = amountPayed;
        this.creditList = creditList;
    }

    public Participant() {
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public int getAmountPayed() {
        return amountPayed;
    }

    public void setAmountPayed(int amountPayed) {
        this.amountPayed = amountPayed;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void addCredit(Credit credit) {
        if (credit != null)
            creditList.add(credit);
    }

    public ArrayList<Credit> creditList() {
        return creditList;
    }
}
