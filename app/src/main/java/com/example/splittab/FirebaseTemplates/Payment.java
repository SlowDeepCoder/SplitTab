package com.example.splittab.FirebaseTemplates;

public class Payment {
    private int day, month, year;
    private int amount;
    private String description;
    private String userName, userUID;
    private String key;

    public Payment(int day, int month, int year, int amount, String description, String userUID, String userName) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.amount = amount;
        this.description = description;
        this.userUID = userUID;
        this.userName = userName;
    }

    public Payment(){}

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
