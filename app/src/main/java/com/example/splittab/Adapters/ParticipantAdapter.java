package com.example.splittab.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.splittab.FirebaseTemplates.Participant;
import com.example.splittab.R;

import java.util.ArrayList;

public class ParticipantAdapter extends ArrayAdapter<Participant> {
    private ArrayList<Participant> participantList;

    public ParticipantAdapter(@NonNull Context context, int resource, ArrayList<Participant> participantList) {
        super(context, resource, participantList);
        this.participantList = participantList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.participant_list_item, parent, false);

        TextView participantName = (TextView) convertView.findViewById(R.id.participant_name_textview);
        TextView participantCredit = (TextView) convertView.findViewById(R.id.participant_credit_textview);
        ConstraintLayout background = (ConstraintLayout) convertView.findViewById(R.id.particpant_layout_background);

        Participant participant = getItem(position);
        participantName.setText(participantList.get(position).getUserName());

        int credit = participantList.get(position).getCredit();
        if (credit < 0)
            background.setBackgroundColor(getContext().getResources().getColor(R.color.credit_minus_red));
        else if (credit > 0)
            background.setBackgroundColor(getContext().getResources().getColor(R.color.credit_plus_green));
        else
            background.setBackgroundColor(getContext().getResources().getColor(R.color.white));

        StringBuilder builder = new StringBuilder();
        if (credit > 0)
            builder.append("+");
        builder.append(credit);
        builder.append(" kr");
        participantCredit.setText(builder.toString());

        return convertView;
    }
}
