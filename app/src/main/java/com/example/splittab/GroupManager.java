package com.example.splittab;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.ActionMenuItemView;

import com.example.splittab.Adapters.CreditAdapter;
import com.example.splittab.Adapters.PaymentAdapter;
import com.example.splittab.Dialogs.GroupListDialog;
import com.example.splittab.FirebaseTemplates.Credit;
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
import java.util.Collections;
import java.util.Random;

public class GroupManager {
    private static final GroupManager GROUP_MANAGER = new GroupManager();
    private ArrayList<Group> groupList = new ArrayList<>();
    private Group currentGroup;
    private Participant currentParticipant;

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
            setCurrentParticipant();


            AddPaymentFragment.paymentAdapter = new PaymentAdapter(context, R.layout.payment_list_item, getCurrentGroup().getPaymentList());
            AddPaymentFragment.paymentListView.setAdapter(AddPaymentFragment.paymentAdapter);
            AddPaymentFragment.paymentAdapter.notifyDataSetChanged();

            HistoryFragment.historyAdapter = new PaymentAdapter(context, R.layout.payment_list_item, getCurrentGroup().getPaymentList());
            HistoryFragment.historyListView.setAdapter(AddPaymentFragment.paymentAdapter);
            HistoryFragment.historyAdapter.notifyDataSetChanged();


            OverviewFragment.creditAdapter = new CreditAdapter(context, R.layout.payment_list_item, getCurrentParticipant().creditList());
            OverviewFragment.participantListView.setAdapter(OverviewFragment.creditAdapter);
            OverviewFragment.creditAdapter.notifyDataSetChanged();

            AddPaymentFragment.resetSelectedBooleans();
        }
    }

    public Group getCurrentGroup() {
        return currentGroup;
    }


    private void setCurrentParticipant() {
        if (currentGroup == null) {
            currentParticipant = null;
            return;
        }
        for (Participant p : currentGroup.getParticipantList()) {
            if (p.getUserUID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                currentParticipant = p;
                Log.d("setCurrentParticipant", "currentParticipant satt");
                Log.d("setCurrentParticipant", currentParticipant.creditList().size() + "");
            }
        }
    }

    public Participant getCurrentParticipant() {
        return currentParticipant;
    }

    @SuppressLint("RestrictedApi")
    public void setCurrentGroup(int index, Activity activity) {
        ActionMenuItemView item = (ActionMenuItemView) activity.findViewById(R.id.actionbarGroup);
        if (index < groupList.size() && index >= 0) {
            currentGroup = groupList.get(index);
            setCurrentParticipant();
            AddPaymentFragment.resetSelectedBooleans();

            AddPaymentFragment.paymentAdapter = new PaymentAdapter(activity, R.layout.payment_list_item, currentGroup.getPaymentList());
            if (AddPaymentFragment.paymentListView != null)
                AddPaymentFragment.paymentListView.setAdapter(AddPaymentFragment.paymentAdapter);
            AddPaymentFragment.paymentAdapter.notifyDataSetChanged();

            HistoryFragment.historyAdapter = new PaymentAdapter(activity, R.layout.payment_list_item, currentGroup.getPaymentList());
            if (HistoryFragment.historyListView != null)
                HistoryFragment.historyListView.setAdapter(AddPaymentFragment.paymentAdapter);
            HistoryFragment.historyAdapter.notifyDataSetChanged();

            OverviewFragment.creditAdapter = new CreditAdapter(activity, R.layout.payment_list_item, currentParticipant.creditList());
            if (OverviewFragment.participantListView != null)
                OverviewFragment.participantListView.setAdapter(OverviewFragment.creditAdapter);
            OverviewFragment.creditAdapter.notifyDataSetChanged();

            if (item != null)
                item.setTitle(currentGroup.getName());
        } else {
            currentGroup = null;
            AddPaymentFragment.paymentAdapter = null;
            HistoryFragment.historyAdapter = null;
            OverviewFragment.creditAdapter = null;

            if (AddPaymentFragment.paymentListView != null)
                AddPaymentFragment.paymentListView.setAdapter(null);
            if (HistoryFragment.historyListView != null)
                HistoryFragment.historyListView.setAdapter(null);
            if (OverviewFragment.participantListView != null)
                OverviewFragment.participantListView.setAdapter(null);

            if (item != null)
                item.setTitle(activity.getResources().getString(R.string.no_group));
        }
    }

    public void loadGroupsFromFireBase(final Activity activity) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<String> groupKeyList = new ArrayList<>();

        DatabaseReference countGroupsReference = database.getReference("users").child(user.getUid()).child("groups");
        countGroupsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {                  /////
                        groupKeyList.add(dataSnap.getKey());                                    /////  Hittar alla nycklar som finns hos användaren och lägger i dem i en lista
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
                                    Participant participant = dataSnap2.getValue(Participant.class);
                                    Log.d("Load participant", dataSnap2.toString());
                                    for (DataSnapshot dataSnap3 : dataSnap2.child("credit").getChildren()) {
                                        participant.addCredit(dataSnap3.getValue(Credit.class));
                                        Log.d("Load credit", dataSnap3.toString());
                                    }
                                    group.addParticipant(participant);
                                }
                                for (DataSnapshot dataSnap4 : dataSnap.child("payments").getChildren()) {
                                    group.addPayment(dataSnap4.getValue(Payment.class));
                                }
                                Collections.reverse(group.getPaymentList());
                                groupList.add(group);                          /////  Söker efter varje nyckel och laddar in alla grupper i en lista
                            }
                        }
                    }
                    setCurrentGroup(0, activity);
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
                    if (newPayment) {
                        group.addPayment(snapshot.getValue(Payment.class));
                        AddPaymentFragment.paymentAdapter.notifyDataSetChanged();
                        HistoryFragment.historyAdapter.notifyDataSetChanged();
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

            database.getReference("groups").child(group.getKey()).child("participants").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    boolean newParticipant = true;
                    for (int i = 0; i < group.getParticipantList().size(); i++) {
                        Participant participant = group.getParticipantList().get(i);
                        if (participant.getUserUID().equals(snapshot.getValue(Participant.class).getUserUID()))
                            newParticipant = false;
                    }
                    if (newParticipant) {
                        Participant participant = snapshot.getValue(Participant.class);
                        for (DataSnapshot snapshot2 : snapshot.child("credit").getChildren()) {
                            participant.addCredit(snapshot2.getValue(Credit.class));
                        }
                        groupManager.getCurrentParticipant().addCredit(new Credit(0, participant.getUserUID(), participant.getUserName()));
                        group.addParticipant(snapshot.getValue(Participant.class));
                        GroupListDialog.groupAdapter.notifyDataSetChanged();
                        OverviewFragment.creditAdapter.notifyDataSetChanged();

                        updateCreditListeners(database, group);

                        AddPaymentFragment.resetSelectedBooleans();
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    Participant removedParticipant = snapshot.getValue(Participant.class);
                    for (int i = 0; i < group.getParticipantList().size(); i++) {
                        Participant participant = group.getParticipantList().get(i);
                        if (participant.getUserUID().equals(removedParticipant.getUserUID())) {
                            group.removeParticipant(participant);

                            for (Participant p : group.getParticipantList()) {
                                for (Credit c : p.creditList()) {
                                    if (c.getUserUID().equals(removedParticipant.getUserUID())) {
                                        p.removeCredit(c);
                                        database.getReference("groups").child(group.getKey()).child("participants").child(p.getUserUID()).child("credit").child(removedParticipant.getUserUID()).removeValue();
                                        break;
                                    }
                                }
                            }

                            if (OverviewFragment.creditAdapter != null)
                                OverviewFragment.creditAdapter.notifyDataSetChanged();
                            if (GroupListDialog.groupAdapter != null)
                                GroupListDialog.groupAdapter.notifyDataSetChanged();


                            AddPaymentFragment.resetSelectedBooleans();

                            database.getReference("groups").child(group.getKey()).child("participants").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(!snapshot.exists()){
                                        database.getReference("groups").child(group.getKey()).removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            break;
                        }
                    }
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            updateCreditListeners(database, group);
        }
    }


    private void updateCreditListeners(FirebaseDatabase database, Group group) {
        for (final Participant p : group.getParticipantList()) {
            for (final Credit c : p.creditList()) {
                database.getReference("groups").child(group.getKey()).child("participants").child(p.getUserUID()).child("credit").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.exists()) {
                            Log.d("onChildChanged", snapshot.toString());
                            Credit credit = snapshot.getValue(Credit.class);
                            for (Credit c2 : p.creditList()) {
                                if (c.getUserUID().equals(c2.getUserUID())) {
                                    c2.setAmount(credit.getAmount());
                                }
                            }
                            OverviewFragment.creditAdapter.notifyDataSetChanged();
                        }
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

    public void leaveCurrentGroup(Activity activity) {
        if (currentGroup == null) {
            Toast.makeText(activity, R.string.no_selcted_group, Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < currentParticipant.creditList().size(); i++) {
            Credit c = currentParticipant.creditList().get(i);
            if (c.getAmount() != 0) {
                Toast.makeText(activity, R.string.credit_not_zero, Toast.LENGTH_SHORT).show();
                return;
            }
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("groups").child(currentGroup.getKey()).child("participants").child(currentParticipant.getUserUID()).removeValue();
        database.getReference("users").child(currentParticipant.getUserUID()).child("groups").child(currentGroup.getKey()).removeValue();
        groupList.remove(currentGroup);
        setCurrentGroup(0, activity);
        setCurrentParticipant();
    }
}
