package com.example.airqa.models;

public class User {
    private final String client_id = "open_remote";
    private String email;
    private String username;
    private String passwrod;
    private final String grant_type = "password";

    public User(String username, String passwrod) {
        this.username = username;
        this.passwrod = passwrod;
    }

    public String getClient_id() {
        return client_id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswrod() {
        return passwrod;
    }

    public void setPasswrod(String passwrod) {
        this.passwrod = passwrod;
    }

    public String getGrant_type() {
        return grant_type;
    }
}
