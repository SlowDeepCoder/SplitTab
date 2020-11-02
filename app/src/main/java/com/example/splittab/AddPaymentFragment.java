package com.example.splittab;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.example.splittab.FirebaseTemplates.Group;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class AddPaymentFragment extends Fragment {
    private Spinner daySpinner, monthSpinner, yearSpinner, groupSpinner;
    private Button addPaymentButton;
    private PaymentManager paymentManager;
    private ArrayList<String> array = new ArrayList<>();
    public static ArrayAdapter<String> adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_payment_layout, container, false);

        paymentManager = PaymentManager.getInstance();
        findViewsByTheirId(view);
        setOnClickListeners();
        setSpinners();

        return view;
    }

    private void setOnClickListeners() {
        addPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentManager.addPayment(new Payment());
            }
        });
    }

    private void setSpinners() {
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


        for(Group group: GroupManager.getInstance().getGroupArrayList())
            array.add(group.getName());

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, array);
        groupSpinner.setAdapter(adapter);
    }

    private void findViewsByTheirId(View view) {
        daySpinner = (Spinner)view.findViewById(R.id.days_spinner);
        monthSpinner = (Spinner)view.findViewById(R.id.months_spinner);
        yearSpinner = (Spinner)view.findViewById(R.id.year_spinner);
        groupSpinner = (Spinner)view.findViewById(R.id.add_payment_group_spinner);
        addPaymentButton = (Button)view.findViewById(R.id.add_payment_button);
    }
}