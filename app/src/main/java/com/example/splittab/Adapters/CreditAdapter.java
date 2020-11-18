package com.example.splittab.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.splittab.FirebaseTemplates.Credit;
import com.example.splittab.FirebaseTemplates.Participant;
import com.example.splittab.R;

import java.util.ArrayList;

public class CreditAdapter extends ArrayAdapter<Credit> {
    private ArrayList<Credit> creditList;

    public CreditAdapter(@NonNull Context context, int resource, ArrayList<Credit> creditList) {
        super(context, resource, creditList);
        this.creditList = creditList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.participant_list_item, parent, false);

        TextView creditName = (TextView) convertView.findViewById(R.id.participant_name_textview);
        TextView creditAmount = (TextView) convertView.findViewById(R.id.participant_credit_textview);
        ConstraintLayout background = (ConstraintLayout) convertView.findViewById(R.id.particpant_layout_background);

        Credit credit = getItem(position);
        creditName.setText(credit.getUserName());

        double amount = credit.getAmount();
        if (amount < 0)
            background.setBackgroundColor(getContext().getResources().getColor(R.color.credit_minus_red));
        else if (amount > 0)
            background.setBackgroundColor(getContext().getResources().getColor(R.color.credit_plus_green));
        else
            background.setBackgroundColor(getContext().getResources().getColor(R.color.white));

        StringBuilder builder = new StringBuilder();
        if (amount > 0)
            builder.append("+");
        builder.append(amount);
        builder.append(" kr");
        creditAmount.setText(builder.toString());

        return convertView;
    }
}
