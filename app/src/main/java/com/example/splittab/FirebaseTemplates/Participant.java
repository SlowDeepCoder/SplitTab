package com.example.splittab.FirebaseTemplates;

public class Participant {
    private String userUID, userName;
    private int amountPayed;
    private int credit;

    public Participant(String userUID, String userName, int amountPayed, int credit) {
        this.userUID = userUID;
        this.userName = userName;
        this.amountPayed = amountPayed;
        this.credit = credit;
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

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
