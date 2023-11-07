package com.example.airqa.models;

public class User {
    private final String realm = "master";
    private String realmId;
    private String id;
    private String email;
    private boolean enabled;
    private  Integer createdOn;
    private boolean serviceAccount;
    private String username;


    public User(String username) {
        this.username = username;
    }
}
