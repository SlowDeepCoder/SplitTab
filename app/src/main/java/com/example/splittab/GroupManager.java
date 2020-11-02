package com.example.splittab;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.splittab.FirebaseTemplates.Group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    public void add(Group group) {
        groupList.add(group);

        if(currentGroup == null)
            currentGroup = group;
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

        DatabaseReference findGroupReference = database.getReference("groups");
        findGroupReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    groupList.clear();
                    for (String key : groupKeyList) {
                        for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                            if (key.equals(dataSnap.getKey())) {                               /////
                                Group group = dataSnap.getValue(Group.class);                  /////  Söker efter varje nyckel och laddar in alla grupper i en lista
                                groupList.add(group);                                          /////
                            }
                        }

                    }
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
}
