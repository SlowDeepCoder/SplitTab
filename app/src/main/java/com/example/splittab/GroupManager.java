package com.example.splittab;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.ActionMenuItemView;

import com.example.splittab.Adapters.PaymentAdapter;
import com.example.splittab.Dialogs.GroupListDialog;
import com.example.splittab.FirebaseTemplates.Group;
import com.example.splittab.FirebaseTemplates.Participant;
import com.example.splittab.FirebaseTemplates.Payment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class GroupManager {
    private static final GroupManager GROUP_MANAGER = new GroupManager();
    private ArrayList<Group> groupList = new ArrayList<>();
    private Group currentGroup;

    private GroupManager() {
    }

    public static GroupManager getInstance() {
        return GROUP_MANAGER;
    }

    public ArrayList<Group> getGroupArrayList() {
        return groupList;
    }

    public void addGroup(Group group, Context context) {
        groupList.add(group);

        if (currentGroup == null) {
            currentGroup = group;
            AddPaymentFragment.paymentAdapter = new PaymentAdapter(context, R.layout.payment_list_item, GroupManager.getInstance().getCurrentGroup().getPaymentList());
            AddPaymentFragment.paymentListView.setAdapter(AddPaymentFragment.paymentAdapter);
            AddPaymentFragment.paymentAdapter.notifyDataSetChanged();

            HistoryFragment.historyAdapter = new PaymentAdapter(context, R.layout.payment_list_item, GroupManager.getInstance().getCurrentGroup().getPaymentList());
            HistoryFragment.historyListView.setAdapter(AddPaymentFragment.paymentAdapter);
            HistoryFragment.historyAdapter.notifyDataSetChanged();
        }
    }


    public Group getCurrentGroup() {
        return currentGroup;
    }

    public void setCurrentGroup(Group currentGroup) {
        this.currentGroup = currentGroup;
    }

    public void setCurrentGroup(int index) {
        if (index < groupList.size() && index >= 0) {
            this.currentGroup = groupList.get(index);
            Log.d("setCurrentGroup", "currentGroup satt till " + currentGroup.getName());
        }
    }


    public void loadGroupsFromFireBase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<String> groupKeyList = new ArrayList<>();

        DatabaseReference countGroupsReference = database.getReference("users").child(user.getUid()).child("groups");
        countGroupsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {                  /////
                        groupKeyList.add(dataSnap.getKey());                                    /////  Hittar alla nycklar som finns hos användaren och lägger i ndem i en lista
                    }                                                                           /////
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        DatabaseReference groupReference = database.getReference("groups");
        groupReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    groupList.clear();
                    for (String key : groupKeyList) {
                        for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                            if (key.equals(dataSnap.getKey())) {
                                Group group = dataSnap.getValue(Group.class);
                                for (DataSnapshot dataSnap2 : dataSnap.child("participants").getChildren()) {
                                    group.addParticipant(dataSnap2.getValue(Participant.class));
                                }
                                for (DataSnapshot dataSnap3 : dataSnap.child("payments").getChildren()) {
                                    group.addPayment(dataSnap3.getValue(Payment.class));
                                }
                                groupList.add(group);                          /////  Söker efter varje nyckel och laddar in alla grupper i en lista
                            }
                        }
                    }
                    setCurrentGroup(0);
                    setFirebasePaymentAndParticipantsListeners();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }


    public static String generateKey() {
        StringBuilder builder = new StringBuilder();
        String selection = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random rnd = new Random();
        for (int i = 0; i < 5; i++) {
            int index = rnd.nextInt(selection.length());
            builder.append(selection.charAt(index));
        }
        return builder.toString();
    }

    public void setFirebasePaymentAndParticipantsListeners() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final GroupManager groupManager = GroupManager.getInstance();

        for (final Group group : groupManager.getGroupArrayList()) {
            database.getReference("groups").child(group.getKey()).child("payments").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    boolean newPayment = true;
                    for (int i = 0; i < group.getPaymentList().size(); i++) {
                        Payment payment = group.getPaymentList().get(i);
                        if (payment.getKey().equals(snapshot.getValue(Payment.class).getKey()))
                            newPayment = false;
                    }
                    if(newPayment) {
                        group.addPayment(snapshot.getValue(Payment.class));
                        AddPaymentFragment.paymentAdapter.notifyDataSetChanged();
                        HistoryFragment.historyAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Log.d("onChildChanged", snapshot.toString());

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            database.getReference("groups").child(group.getKey()).child("participants").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    boolean newParticipant = true;
                    for (int i = 0; i < group.getParticipantList().size(); i++) {
                        Participant participant = group.getParticipantList().get(i);
                        if (participant.getUserUID().equals(snapshot.getValue(Participant.class).getUserUID()))
                            newParticipant = false;
                    }
                    if(newParticipant) {
                        group.addParticipant(snapshot.getValue(Participant.class));
                        GroupListDialog.groupAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

}
