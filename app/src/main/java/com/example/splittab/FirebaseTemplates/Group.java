package com.example.splittab.FirebaseTemplates;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.splittab.AddPaymentFragment;
import com.example.splittab.Dialogs.GroupListDialog;
import com.example.splittab.GroupManager;
import com.example.splittab.OverviewFragment;
import com.example.splittab.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Group {
    private String key;
    private String name;
    private String creator;
    private ArrayList<Participant> participantList = new ArrayList<>();
    private ArrayList<Payment> paymentList = new ArrayList<>();

    public Group(String key, String name, String creator) {
        this.key = key;
        this.name = name;
        this.creator = creator;
    }

    public Group() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String participants) {
        this.creator = participants;
    }

    public void addParticipant(Participant participant) {
        participantList.add(participant);
    }

    public ArrayList<Participant> getParticipantList() {
        return participantList;
    }

    public ArrayList<Payment> getPaymentList() {
        return paymentList;
    }

    public void addPayment(Payment payment) {
        paymentList.add(payment);
    }

    public void createPaymentAndSaveToFireBase(final Payment payment, Context context) {
        paymentList.add(payment);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final String paymentKey = database.getReference().push().getKey();
        payment.setKey(paymentKey);

        database.getReference("groups").child(key).child("participants").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final Participant currentParticipant = dataSnapshot.getValue(Participant.class);

                    int newAmountPayed = currentParticipant.getAmountPayed() + payment.getAmount();
                    int newCredit = currentParticipant.getCredit() + payment.getAmount();
                    currentParticipant.setAmountPayed(newAmountPayed);
                    currentParticipant.setCredit(newCredit);

                    database.getReference("groups").child(key).child("payments").child(paymentKey).setValue(payment);
                    database.getReference("groups").child(key).child("participants").child(user.getUid()).child("amountPayed").setValue(newAmountPayed);
                    database.getReference("groups").child(key).child("participants").child(user.getUid()).child("credit").setValue(newCredit);

                    if (participantList.size() > 1) {
                        database.getReference("groups").child(key).child("participants").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot dataSnap : snapshot.getChildren()) {
                                        Participant p = dataSnap.getValue(Participant.class);
                                        if (p.getUserUID().equals(currentParticipant.getUserUID()))
                                            continue;

                                        int newCredit = p.getCredit() - payment.getAmount() / (participantList.size() - 1);
                                        p.setCredit(newCredit);
                                        database.getReference("groups").child(key).child("participants").child(p.getUserUID()).child("credit").setValue(newCredit);

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //
        //   Notify on data changed!
        //
    }
}
