package com.example.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "User")
public class User {
    @Id
    private String id;
    private String name;
    private String password;
    private String email;
    private String phoneNumber;
    private String profilePicture;
    private UserRole role;
    private String status;
    private Date lastActive;
    private Date createdAt;
    private Date updatedAt;
    private List<UserActivity> activities;
    private long sessionTimeoutMs = 300000; // 5 minutes timeout

    public User() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.status = "OFFLINE";
        this.lastActive = new Date(0);
        this.activities = new ArrayList<>();
    }

    public User(String name, String password, String email, UserRole role) {
        this();
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public User(String name, String password, String email, String phoneNumber, String profilePicture, UserRole role, String status) {
        this(name, password, email, role);
        this.phoneNumber = phoneNumber;
        this.profilePicture = profilePicture;
        this.status = status != null ? status : "OFFLINE";
    }

    public boolean isOnline() {
        if ("DEACTIVATED".equals(status)) {
            return false;
        }
        if (lastActive == null) {
            return false;
        }
        long timeSinceLastActive = System.currentTimeMillis() - lastActive.getTime();
        return timeSinceLastActive <= sessionTimeoutMs;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
        if ("ONLINE".equals(status)) {
            this.lastActive = new Date();
        }
    }

    public Date getLastActive() { return lastActive; }
    public void setLastActive(Date lastActive) { this.lastActive = lastActive; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    public List<UserActivity> getActivities() { return activities; }
    public void setActivities(List<UserActivity> activities) { this.activities = activities; }

    public long getSessionTimeoutMs() { return sessionTimeoutMs; }
    public void setSessionTimeoutMs(long sessionTimeoutMs) { this.sessionTimeoutMs = sessionTimeoutMs; }

    public void addActivity(String action) {
        if (this.activities == null) {
            this.activities = new ArrayList<>();
        }
        this.activities.add(new UserActivity(action));
    }

    @Override
    public String toString() {
        return "User{id='" + id + "', name='" + name + "', email='" + email + "', phone='" + phoneNumber + 
               "', role=" + role + ", status=" + status + ", lastActive=" + lastActive + 
               ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", activities=" + activities + "}";
    }
}