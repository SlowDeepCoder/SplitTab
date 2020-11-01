package com.example.splittab;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.DialogFragment;

import com.example.splittab.FirebaseTemplates.Group;

import java.util.ArrayList;

public class GroupsDialog extends DialogFragment {
    private ListView listView;
    private ArrayList<String> array = new ArrayList<>();
    private ArrayAdapter<String> adapter;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.groups_dialog_layout, null);
        builder.setView(view).setTitle("Groups");

        for(Group group: GroupManager.getInstance().getGroupArrayList())
            array.add(group.getName());

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, array);

        listView = (ListView)view.findViewById(R.id.groupsDialogListView);
        listView.setAdapter(adapter);


        setOnClickListerners(view);



        return builder.create();
    }

    private void setOnClickListerners(View view) {
        view.findViewById(R.id.create_group_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewGroupDialog dialog = new NewGroupDialog();
                dialog.show(getActivity().getSupportFragmentManager(), "");
            }
        });

        view.findViewById(R.id.join_group_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ////
                ////   Not done yet
                ////
            }
        });
    }

}
