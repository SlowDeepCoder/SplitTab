package com.example.splittab;

import java.util.ArrayList;
import java.util.Date;

public class Payments {
    private static Payments payments = new Payments();
    private ArrayList<Payment> paymentsList = new ArrayList<>();

    public final static int SWEDISH_KRONA = 0, AMERICAN_DOLLAR = 1;



    private Payments(){
        if(payments!=null) return;
    }

    public static Payments getInstance(){
        return payments;
    }

    public void addPayment(int currency, int sum, String description, Date date){
        Payment p = new Payment();
        switch (currency){
            case SWEDISH_KRONA:
                break;
            case AMERICAN_DOLLAR:
                break;
        }
        paymentsList.add(p);
    }

    public void addPayment(Payment payment){
        paymentsList.add(payment);
    }
}
