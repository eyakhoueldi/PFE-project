package com.example.backend.models;

import java.util.Date;
import java.util.List;

public class UserDto {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String profilePicture;
    private UserRole role;
    private String status;
    private Date lastActive;
    private Date createdAt;
    private Date updatedAt;
    private List<UserActivity> activities;

    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.profilePicture = user.getProfilePicture();
        this.role = user.getRole();
        this.status = user.getStatus();
        this.lastActive = user.getLastActive();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
        this.activities = user.getActivities();
    }

    public UserDto() {
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getLastActive() { return lastActive; }
    public void setLastActive(Date lastActive) { this.lastActive = lastActive; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    public List<UserActivity> getActivities() { return activities; }
    public void setActivities(List<UserActivity> activities) { this.activities = activities; }
}