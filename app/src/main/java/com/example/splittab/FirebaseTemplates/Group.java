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
    //Arraylist med Pictures

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
        paymentList.add(0, payment);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final double splitAmount = payment.getAmount() / payment.participantsList().size();

        final String paymentKey = database.getReference().push().getKey();
        payment.setKey(paymentKey);


        database.getReference("groups").child(key).child("payments").child(paymentKey).setValue(payment);


        database.getReference("groups").child(key).child("participants").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (Participant p : payment.participantsList()) {
                    database.getReference("groups").child(key).child("payments").child(paymentKey).child("participants").child(p.getUserUID()).setValue(p.getUserName());

                    double oldAmountOtherUser = 0.0;
                    double oldAmountCurrentUser = 0.0;
                    double newAmountOtherUser = 0.0;
                    double newAmountCurrentUse = 0.0;
                    if (!p.getUserUID().equals(user.getUid())) {
                        try {
                            oldAmountOtherUser = snapshot.child(p.getUserUID()).child("credit").child(user.getUid()).child("amount").getValue(Double.class);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } finally {
                            newAmountOtherUser = oldAmountOtherUser - splitAmount;
                        }

                        try {
                            oldAmountCurrentUser = snapshot.child(user.getUid()).child("credit").child(p.getUserUID()).child("amount").getValue(Double.class);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } finally {
                            newAmountCurrentUse = oldAmountCurrentUser + splitAmount;
                        }

                        database.getReference("groups").child(key).child("participants").child(p.getUserUID()).child("credit").child(user.getUid()).child("amount").setValue(newAmountOtherUser);
                        database.getReference("groups").child(key).child("participants").child(user.getUid()).child("credit").child(p.getUserUID()).child("amount").setValue(newAmountCurrentUse);
                    }


//                    else {
//                        for (Participant p2 : payment.participantsList()) {
//                            try {
//                                oldAmount = snapshot.child(user.getUid()).child("credit").child(p2.getUserUID()).child("amount").getValue(Double.class);
//                            } catch (NullPointerException e) {
//                                e.printStackTrace();
//                            } finally {
//                                newAmount = oldAmount + splitAmount;
//                            }
//                            if (!p2.getUserUID().equals(user.getUid()))
//                                database.getReference("groups").child(key).child("participants").child(p.getUserUID()).child("credit").child(p2.getUserUID()).child("amount").setValue(newAmount);
//                        }
//                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        database.getReference("groups").child(key).child("participants").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    final Participant currentParticipant = dataSnapshot.getValue(Participant.class);
//
//
//                    database.getReference("groups").child(key).child("payments").child(paymentKey).setValue(payment);
//
//                    double splitAmount = payment.getAmount()/payment.participantsList().size();
//                    for (Participant p : payment.participantsList()) {
//                        database.getReference("groups").child(key).child("payments").child(paymentKey).child("participants").child(p.getUserUID()).setValue(p.getUserName());
//
//
//                        database.getReference("groups").child(key).child("participants").child(p.getUserUID()).child("credit")
//                    }
//
//
//                    int newAmountPayed = currentParticipant.getAmountPayed() + payment.getAmount();
//                    database.getReference("groups").child(key).child("participants").child(user.getUid()).child("amountPayed").setValue(newAmountPayed);
//
//                    for (Participant p : payment.getTheSelectedParticipantsList()) {
//                        if (dataSnapshot.child("credit").child(p.getUserUID()).exists()) {
//                            Credit credit = dataSnapshot.child("credit").child(p.getUserUID()).getValue(Credit.class);
//                            int newCredit = credit.getCredit() + payment.getAmount();
//                            database.getReference("groups").child(key).child("participants")
//                        }
//                    }
//
//
//                    currentParticipant.setAmountPayed(newAmountPayed);
//                    currentParticipant.setCredit(newCredit);
//
//                    database.getReference("groups").child(key).child("participants").child(user.getUid()).child("credit").setValue(newCredit);
//
//
//                    if (participantList.size() > 1) {
//                        database.getReference("groups").child(key).child("participants").addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                if (snapshot.exists()) {
//                                    for (DataSnapshot dataSnap : snapshot.getChildren()) {
//                                        Participant p = dataSnap.getValue(Participant.class);
//                                        if (p.getUserUID().equals(currentParticipant.getUserUID()))
//                                            continue;
//
//                                        int newCredit = p.getCredit() - payment.getAmount() / (participantList.size() - 1);
//                                        p.setCredit(newCredit);
//                                        database.getReference("groups").child(key).child("participants").child(p.getUserUID()).child("credit").setValue(newCredit);
//
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
    }
//        });
//
//
    //
    //   Notify on data changed!
    //
//    }
}
