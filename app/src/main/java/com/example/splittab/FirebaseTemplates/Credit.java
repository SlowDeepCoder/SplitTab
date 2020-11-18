package com.example.splittab.FirebaseTemplates;

public class Credit {
    private double amount;
    private String userUID, userName;

    public Credit(double amount, String userUID, String userName) {
        this.amount = amount;
        this.userUID = userUID;
        this.userName = userName;
    }

    public Credit() {
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
