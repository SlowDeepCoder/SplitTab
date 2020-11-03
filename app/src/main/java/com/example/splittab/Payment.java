package com.example.splittab;

public class Payment {
    private int day, year;
    private int amount, currency;
    private String description, month;
    //TODO man måste få reda på VEM som är ägaren av betalningen
    //TODO variabel till användar ID
    //TODO tom konstrutor

    public Payment(int day, String month, int year, int amount, String description) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.amount = amount;
        this.description = description;
    }
}

