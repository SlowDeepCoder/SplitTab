package com.example.splittab;

import java.util.ArrayList;
import java.util.Date;

public class PaymentManager {
    private final static PaymentManager paymentManager = new PaymentManager();
    private ArrayList<Payment> paymentList = new ArrayList<>();

    public final static int SWEDISH_KRONA = 0, AMERICAN_DOLLAR = 1;


    private PaymentManager(){}

    public static PaymentManager getInstance(){
        return paymentManager;
    }

    public void addPayment(Payment payment){
        paymentList.add(payment);
    }
}
