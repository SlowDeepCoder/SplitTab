package com.example.splittab.FirebaseTemplates;

import java.util.ArrayList;

public class Group {
    private String key;
    private String name;
    private String creator;
    private ArrayList<String> participants = new ArrayList<>();

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
        participants.add(userUID);
    }

    public ArrayList<String> getParticipantList(){
        return participants;
    }
}
