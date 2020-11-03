package com.example.splittab.FirebaseTemplates;

import android.content.Context;
import android.widget.Toast;

import com.example.splittab.Dialogs.GroupListDialog;
import com.example.splittab.GroupManager;
import com.example.splittab.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Group {
    private String key;
    private String name;
    private String creator;
    private ArrayList<String> participantList = new ArrayList<>();
    private ArrayList<Payment> paymentList = new ArrayList<>();

    public Group(String key, String name, String creator) {
        this.key = key;
        this.name = name;
        this.creator = creator;
    }

    public Group(){}

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

    public void addParticipant(String userUID){
        participantList.add(userUID);
    }

    public ArrayList<String> getParticipantList(){
        return participantList;
    }

    public ArrayList<Payment> getPaymentList(){
        return paymentList;
    }

    public void createPaymentAndSaveToFireBase(Payment payment, Context context){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String paymentKey = database.getReference().push().getKey();
        payment.setKey(paymentKey);

        database.getReference("groups").child(key).child("payments").child(paymentKey).setValue(payment);
        paymentList.add(payment);

        //
        //   Notify on data changed!
        //
    }
}
