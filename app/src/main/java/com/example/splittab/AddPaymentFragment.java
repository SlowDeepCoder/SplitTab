package com.example.splittab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AddPaymentFragment extends Fragment {
    private Spinner daySpinner, monthSpinner, yearSpinner;
    private Button addPaymentButton;
    private PaymentManager paymentManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_payment_layout, container, false);

        paymentManager = PaymentManager.getInstance();
        findViewsByTheirId(view);
        setSpinnerDate();
        setOnClickListerners();

        return view;
    }

    private void setOnClickListerners() {
        addPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentManager.addPayment(new Payment());
            }
        });
    }

    private void setSpinnerDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = Calendar.getInstance().getTime();
        String s = dateFormat.format(today);

        int yearIndex = Integer.parseInt(s.substring(0, 4));
        int monthIndex = Integer.parseInt(s.substring(5, 7));
        int dayIndex = Integer.parseInt(s.substring(8, 10));

        daySpinner.setSelection(dayIndex-1);
        monthSpinner.setSelection(monthIndex-1);
        yearSpinner.setSelection(yearIndex-2018);

    }

    private void findViewsByTheirId(View view) {
        daySpinner = (Spinner)view.findViewById(R.id.days_spinner);
        monthSpinner = (Spinner)view.findViewById(R.id.months_spinner);
        yearSpinner = (Spinner)view.findViewById(R.id.year_spinner);
        addPaymentButton = (Button)view.findViewById(R.id.add_payment_button);
    }

}