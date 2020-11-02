package com.example.splittab.GroupDialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

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

        editText = (EditText)view.findViewById(R.id.editTextGroupName);

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
                dismiss();
            }
        });
    }

    private void createGroupAndSaveToFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String groupName = editText.getText().toString().trim();
        String key = GroupManager.generateKey();                                 // String key = database.getReference("groups").push().getKey();
        Group group = new Group(key, groupName, user.getUid());

        database.getReference("groups").child(key).setValue(group);

        database.getReference("users").child(user.getUid()).child("groups").child(key).setValue(groupName);

        GroupManager groupManager = GroupManager.getInstance();
        groupManager.add(group);

        GroupListDialog.groupAdapter.notifyDataSetChanged();
        AddPaymentFragment.adapter.add(groupName);
        AddPaymentFragment.adapter.notifyDataSetChanged();


    }
}
