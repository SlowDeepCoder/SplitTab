package com.example.splittab.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.splittab.FirebaseTemplates.Payment;
import com.example.splittab.R;

import java.util.ArrayList;

public class PaymentAdapter extends ArrayAdapter<Payment> {
    private ArrayList<Payment> paymentList;

    public PaymentAdapter(@NonNull Context context, int resource, ArrayList<Payment> paymentList) {
        super(context, resource, paymentList);
        this.paymentList = paymentList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.payment_list_item, parent, false);

        TextView paymentName = (TextView)convertView.findViewById(R.id.payment_name_textview);
        TextView paymentSum = (TextView)convertView.findViewById(R.id.payment_sum_textview);
        TextView paymentDate = (TextView)convertView.findViewById(R.id.payment_date_textview);

        Payment payment = getItem(position);
        paymentName.setText(paymentList.get(position).getUser());
        paymentSum.setText(String.valueOf(paymentList.get(position).getAmount()));

        StringBuilder builder = new StringBuilder();
        builder.append(paymentList.get(position).getDay());
        builder.append("-");
        builder.append(paymentList.get(position).getMonth());
        builder.append("-");
        builder.append(paymentList.get(position).getYear());

        paymentDate.setText(builder.toString());

        return convertView;
    }
}
