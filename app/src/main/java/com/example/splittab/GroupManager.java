package com.example.splittab;

import android.util.Log;

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
    }

    public Group getCurrentGroup() {
        return currentGroup;
    }

    public void setCurrentGroup(Group currentGroup) {
        this.currentGroup = currentGroup;
    }

    public void loadGroupsFromFireBase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference reference = database.getReference("users").child(user.getUid()).child("groups");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    groupList.clear();
                    for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                        Group group = dataSnap.getValue(Group.class);
                        groupList.add(group);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
