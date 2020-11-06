package com.example.splittab;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.splittab.Adapters.PaymentAdapter;
import com.example.splittab.FirebaseTemplates.Payment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class AddPaymentFragment extends Fragment {
    private Spinner daySpinner, monthSpinner, yearSpinner;
    private EditText amountEditText, descriptionEditText;
    private Button addPaymentButton;
    private GroupManager paymentManager;
    public static PaymentAdapter paymentAdapter;
    public static ListView paymentListView;
    private GroupManager groupManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_payment_layout, container, false);

        groupManager = GroupManager.getInstance();
        findViewsByTheirId(view);
        setOnClickListeners();
        setSpinners();

        if (groupManager.getCurrentGroup() != null) {
            //Adapter
            paymentAdapter = new PaymentAdapter(getContext(), R.layout.payment_list_item, groupManager.getCurrentGroup().getPaymentList());

            //Connect paymentAdapter to ListView
            paymentListView.setAdapter(paymentAdapter);
        }
        return view;
    }

    private void setOnClickListeners() {
        addPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //paymentManager.addPayment(new Payment(day,month,year,amount, itemDescription));

                if (groupManager.getCurrentGroup() == null) {
                    Toast.makeText(getContext(), getResources().getString(R.string.no_group_selected), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (amountEditText.getText().toString().length() < 1) {
                    Toast.makeText(getContext(), getResources().getString(R.string.enter_amount_to_add_payment), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (descriptionEditText.getText().toString().length() < 1) {
                    Toast.makeText(getContext(), getResources().getString(R.string.enter_description_to_add_payment), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (groupManager.getCurrentGroup() != null) {
                    int day = daySpinner.getSelectedItemPosition() + 1;
                    int month = monthSpinner.getSelectedItemPosition() + 1;
                    int year = yearSpinner.getSelectedItemPosition() + 2018;
                    int amount = Integer.parseInt(amountEditText.getText().toString().trim());
                    String description = descriptionEditText.getText().toString().trim();
                    String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

                    Payment payment = new Payment(day, month, year, amount, description, userUID, userName);
//                    paymentAdapter.add(payment);

                    groupManager.getCurrentGroup().createPaymentAndSaveToFireBase(payment, getContext());

                    amountEditText.setText("");
                    descriptionEditText.setText("");
                    Toast.makeText(getContext(), getResources().getString(R.string.saved_payment), Toast.LENGTH_SHORT).show();

                    if (paymentAdapter != null)
                        Collections.reverse(groupManager.getCurrentGroup().getPaymentList());
                        paymentAdapter.notifyDataSetChanged();
                }
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

        daySpinner.setSelection(dayIndex - 1);
        monthSpinner.setSelection(monthIndex - 1);
        yearSpinner.setSelection(yearIndex - 2018);
    }

    private void findViewsByTheirId(View view) {
        daySpinner = (Spinner) view.findViewById(R.id.days_spinner);
        monthSpinner = (Spinner) view.findViewById(R.id.months_spinner);
        yearSpinner = (Spinner) view.findViewById(R.id.year_spinner);
        amountEditText = (EditText) view.findViewById(R.id.editTextAmount);
        amountEditText.setInputType(InputType.TYPE_CLASS_NUMBER);   //Siffertagentbord
        descriptionEditText = (EditText) view.findViewById(R.id.editTextDescription);
        addPaymentButton = (Button) view.findViewById(R.id.add_payment_button);
        paymentListView = view.findViewById(R.id.lastPaymentListView);
    }
}