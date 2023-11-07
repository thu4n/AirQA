package com.example.airqa.models;

public class User {
    private String realm;
    private String realmId;
    private String id;
    private String email;
    private boolean enabled;
    private  long  createdOn;
    private boolean serviceAccount;
    private String username;


    public User(String username) {
        this.username = username;
    }

    public User(User user) {
        this.realmId = user.realmId;
        this.id = user.id;
        this.email = user.email;
        this.enabled = user.enabled;
        this.createdOn = user.createdOn;
        this.serviceAccount = user.serviceAccount;
        this.username = user.username;
    }



    public String getRealm() {
        return realm;
    }

    public String getRealmId() {
        return realmId;
    }

    public void setRealmId(String realmId) {
        this.realmId = realmId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public long  getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(long  createdOn) {
        this.createdOn = createdOn;
    }

    public boolean isServiceAccount() {
        return serviceAccount;
    }

    public void setServiceAccount(boolean serviceAccount) {
        this.serviceAccount = serviceAccount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
