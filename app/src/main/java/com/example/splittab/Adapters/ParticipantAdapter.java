package com.example.splittab.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

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

        TextView participantName = (TextView)convertView.findViewById(R.id.participant_name_textview);
        TextView participantCredit = (TextView)convertView.findViewById(R.id.participant_credit_textview);

        Participant participant = getItem(position);
        participantName.setText(participantList.get(position).getUserName());

        participantCredit.setText(String.valueOf(participantList.get(position).getCredit()));

        return convertView;
    }
}
