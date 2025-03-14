package com.example.backend.models;

import java.util.Date;

public class UserActivity {
    private String action;
    private String details;
    private Date timestamp;

    // Default Constructor
    public UserActivity() {
        this.timestamp = new Date();
    }

    // Parameterized Constructor
    public UserActivity(String action) {
        this();
        this.action = action;
    }

    public UserActivity(String action, String details) {
        this(action);
        this.details = details;
    }

    // Getters and Setters
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "UserActivity{action='" + action + "', details='" + details + "', timestamp=" + timestamp + "}";
    }
}
