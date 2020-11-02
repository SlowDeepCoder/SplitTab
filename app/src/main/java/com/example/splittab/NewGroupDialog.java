package com.example.splittab;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.splittab.FirebaseTemplates.Group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewGroupDialog extends DialogFragment {
    private EditText editText;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_group_dialog_layout, null);
        builder.setView(view).setTitle(R.string.create_group);

        editText = (EditText)view.findViewById(R.id.editTextGroupName);

        setOnClickListerners(view);

        return builder.create();
    }

    private void setOnClickListerners(View v) {
        v.findViewById(R.id.join_group_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        v.findViewById(R.id.create_group_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveGroupToFirebaseAndGroupManager();
                dismiss();
            }
        });
    }

    private void saveGroupToFirebaseAndGroupManager() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = database.getReference("groups");

        String groupName = editText.getText().toString().trim();
//        String key = database.getReference("groups").push().getKey();
        String key = GroupManager.generateKey();
        Group group = new Group(key, groupName, user.getUid());

        reference.child(key).setValue(group);

        reference = database.getReference("users").child(user.getUid()).child("groups").child(key);

        reference.setValue(group);

        GroupManager groupManager = GroupManager.getInstance();
        groupManager.add(group);


        GroupsDialog.groupAdapter.add(group);
        GroupsDialog.groupAdapter.notifyDataSetChanged();
        AddPaymentFragment.adapter.add(groupName);
        AddPaymentFragment.adapter.notifyDataSetChanged();


    }
}
