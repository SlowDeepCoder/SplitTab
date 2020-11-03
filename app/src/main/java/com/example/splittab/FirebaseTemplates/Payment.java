package com.example.splittab.FirebaseTemplates;

public class Payment {
    private int day, month, year;
    private int amount;
    private String description;
    private String user;
    private String key;

    public Payment(int day, int month, int year, int amount, String description, String user) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.amount = amount;
        this.description = description;
        this.user = user;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
