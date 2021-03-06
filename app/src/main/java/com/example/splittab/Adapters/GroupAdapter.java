package com.example.splittab.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.splittab.FirebaseTemplates.Group;
import com.example.splittab.FirebaseTemplates.Participant;
import com.example.splittab.R;

import java.util.ArrayList;

public class GroupAdapter extends ArrayAdapter<Group> {
    private ArrayList<Group> groupList;

    public GroupAdapter(@NonNull Context context, int resource, ArrayList<Group> groupList) {
        super(context, resource, groupList);
        this.groupList = groupList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.group_list_item, parent, false);

        TextView groupName = (TextView)convertView.findViewById(R.id.group_name);
        TextView groupKey = (TextView)convertView.findViewById(R.id.group_key);
        TextView groupParticipants = (TextView)convertView.findViewById(R.id.group_participants);

        Group group = getItem(position);
        groupName.setText(group.getName());
        groupKey.setText(groupList.get(position).getKey());

        StringBuilder builder = new StringBuilder();
        for(Participant p : group.getParticipantList()){
            builder.append(p.getUserName());
            builder.append(", ");
        }
        builder.deleteCharAt(builder.lastIndexOf(","));
        groupParticipants.setText(builder.toString());

        return convertView;
    }
}
