package com.example.sprint1.model;

public class User {
    private String email;
    private String username;

    public User(String email) {
        this(email, null);
    }

    public User(String email, String username){
        this.email = email;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username != null ? username : "";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return email;
    }

}
