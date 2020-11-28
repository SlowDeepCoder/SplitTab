package com.example.splittab.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.splittab.AddPaymentFragment;
import com.example.splittab.FirebaseTemplates.Participant;
import com.example.splittab.GroupManager;
import com.example.splittab.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SelectParticipantsDialog extends DialogFragment {
    private GroupManager groupManager;

    public SelectParticipantsDialog(){
        groupManager = GroupManager.getInstance();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final ArrayList<Participant> selectedParticipantList = new ArrayList<>();
        final ArrayList<Participant> participantList = groupManager.getCurrentGroup().getParticipantList();

        final String[] usernameList = new String[participantList.size()];
        for (int i = 0; i < usernameList.length; i++) {
            usernameList[i] = participantList.get(i).getUserName();
        }

        boolean[] selected = new boolean[participantList.size()];
        for (int i = 0; i < AddPaymentFragment.selectedBooleans.size(); i++) {
            selected[i] = AddPaymentFragment.selectedBooleans.get(i);
        }
 
        builder.setTitle(getResources().getString(R.string.select_participants)).setMultiChoiceItems(usernameList, selected, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                AddPaymentFragment.selectedBooleans.set(i, b);
            }
        }).setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        return builder.create();
    }
}
