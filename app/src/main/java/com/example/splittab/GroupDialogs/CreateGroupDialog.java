package com.example.splittab.GroupDialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.splittab.AddPaymentFragment;
import com.example.splittab.FirebaseTemplates.Group;
import com.example.splittab.GroupManager;
import com.example.splittab.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class CreateGroupDialog extends DialogFragment {
    private EditText editText;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.create_group_dialog_layout, null);
        builder.setView(view).setTitle(R.string.create_group);

        editText = (EditText) view.findViewById(R.id.editTextGroupName);

        setOnClickListeners(view);

        return builder.create();
    }

    private void setOnClickListeners(View v) {
        v.findViewById(R.id.cancel_group_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        v.findViewById(R.id.create_group_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGroupAndSaveToFirebase();
            }
        });
    }

    private void createGroupAndSaveToFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String groupName = editText.getText().toString().trim();
        if (groupName.length() < 1) {
            Toast.makeText(getContext(), getResources().getString(R.string.enter_name_to_create_group), Toast.LENGTH_SHORT).show();
            return;
        }

        String key = GroupManager.generateKey();
        Group group = new Group(key, groupName, user.getUid());

        database.getReference("groups").child(key).setValue(group);
        database.getReference("groups").child(key).child("participants").child(user.getUid()).setValue(user.getDisplayName());
        database.getReference("users").child(user.getUid()).child("groups").child(key).setValue(groupName);

        group.addParticipant(user.getDisplayName());
        GroupManager groupManager = GroupManager.getInstance();
        groupManager.add(group);

        GroupListDialog.groupAdapter.notifyDataSetChanged();
        dismiss();
    }
}
