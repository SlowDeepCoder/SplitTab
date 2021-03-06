package com.example.splittab.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.splittab.FirebaseTemplates.Credit;
import com.example.splittab.FirebaseTemplates.Group;
import com.example.splittab.FirebaseTemplates.Participant;
import com.example.splittab.FirebaseTemplates.Payment;
import com.example.splittab.FirebaseTemplates.Picture;
import com.example.splittab.GroupManager;
import com.example.splittab.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class JoinGroupDialog extends DialogFragment {
    private EditText editText;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.join_group_dialog_layout, null);
        builder.setView(view).setTitle(R.string.join_group);

        editText = (EditText) view.findViewById(R.id.editTextJEnterFeedback);

        setOnClickListeners(view);

        return builder.create();
    }

    private void setOnClickListeners(View v) {
        v.findViewById(R.id.cancel_join_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        v.findViewById(R.id.leave_feedback_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = editText.getText().toString().trim();
                if (key.length() != 5) {
                    Toast.makeText(getContext(), getResources().getString(R.string.enter_5_char), Toast.LENGTH_SHORT).show();
                    return;
                } else if (checkIfInGroup(key)) {
                    Toast.makeText(getContext(), getResources().getString(R.string.already_in_group), Toast.LENGTH_SHORT).show();
                } else {
                    joinGroupAndSaveToFireBase(key);
                }
            }
        });
    }

    private void joinGroupAndSaveToFireBase(String id) {
        final String key = id;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final GroupManager groupManager = GroupManager.getInstance();

        final DatabaseReference groupsReference = database.getReference("groups");
        groupsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                        if (key.equals(dataSnap.getKey())) {
                            Group group = dataSnap.getValue(Group.class);



                            for (DataSnapshot dataSnap2 : dataSnap.child("participants").getChildren()) {
                                group.addParticipant(dataSnap2.getValue(Participant.class));
                            }
                            for (DataSnapshot dataSnap3 : dataSnap.child("payments").getChildren()) {
                                group.addPayment(dataSnap3.getValue(Payment.class));
                            }

                            for (DataSnapshot dataSnapshot4 : dataSnap.child("imageULR").getChildren()){
                                group.addPictureList(dataSnapshot4.getValue(Picture.class));
                            }

                            ArrayList<Credit> creditList = new ArrayList<>();
                            for(Participant p : group.getParticipantList()){
                                creditList.add(new Credit(0.0, p.getUserUID(), p.getUserName()));
                            }



                            Participant currentParticipant = new Participant(user.getUid(), user.getDisplayName(), 0, creditList);
                            group.addParticipant(currentParticipant);
                            database.getReference("groups").child(key).child("participants").child(currentParticipant.getUserUID()).setValue(currentParticipant);
                            database.getReference("users").child(user.getUid()).child("groups").child(key).setValue(group.getName());

                            for(Credit c : creditList){
                                database.getReference("groups").child(key).child("participants").child(currentParticipant.getUserUID()).child("credit").child(c.getUserUID()).setValue(c);
                                Credit c2 = new Credit(0.0, currentParticipant.getUserUID(), currentParticipant.getUserName());
                                database.getReference("groups").child(key).child("participants").child(c.getUserUID()).child("credit").child(currentParticipant.getUserUID()).setValue(c2);
                            }

                            groupManager.addGroup(group, getContext());
                            GroupListDialog.groupAdapter.notifyDataSetChanged();
                            groupManager.setFirebasePaymentAndParticipantsListeners();
                            dismiss();
                            return;
                        }
                    }

                }
                Toast.makeText(getContext(), getResources().getString(R.string.that_ID_doesnt_exist), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean checkIfInGroup(String key) {
        GroupManager groupManager = GroupManager.getInstance();
        for (Group group : groupManager.getGroupArrayList()) {
            if (key.equals(group.getKey()))
                return true;
        }
        return false;
    }
}
