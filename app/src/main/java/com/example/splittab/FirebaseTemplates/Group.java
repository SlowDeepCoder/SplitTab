package com.example.splittab.FirebaseTemplates;

public class Group {
    private String key;
    private String name;
    private String creator;

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
}
