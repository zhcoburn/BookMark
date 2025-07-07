package com.coburn.fh.dao;

// User class to represent users in the BookMark database
public class User {
    // Private attributes
    private int id;
    private String name;
    private String username;
    private String userpass;
    private double progress;

    // Constructor; progress value is arbritrary and will automatically update later
    public User(int id, String name, String username, String userpass, double progress)
    {
        this.id = id;
        this.name = name;
        this.username = username;
        this.userpass = userpass;
        this.progress = progress;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpass() {
        return userpass;
    }

    public void setUserpass(String userpass) {
        this.userpass = userpass;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    // toString
    public String toString()
    {
        return ("[ID: " + id + ", Name: " + name + ", Username: " + username + ", Password: " + userpass + ", List Progress: " + progress + "]");
    }
}
