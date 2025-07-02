package com.coburn.fh.dao;

public class User {
    private int userId;
    private String name;
    private String username;
    private String userpass;
    private double progress;

    public User(int userId, String name, String username, String userpass, double progress)
    {
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.userpass = userpass;
        this.progress = progress;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String toString()
    {
        return ("[ID: ?, Name: ?, Username: ?, Password: ?, List Progress: ?]", userId, name, username, userpass, progress);
    }
}
