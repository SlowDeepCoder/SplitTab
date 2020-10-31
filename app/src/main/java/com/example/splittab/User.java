package com.example.splittab;

public class User {
    private String name, email, ID;

    public User(String name, String email, String ID) {
        this.name = name;
        this.email = email;
        this.ID = ID;
    }

    public User(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
