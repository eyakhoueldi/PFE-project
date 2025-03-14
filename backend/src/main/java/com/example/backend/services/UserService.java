package com.example.backend.services;

import com.example.backend.models.User;
import com.example.backend.models.UserRole;
import java.util.List;

public interface UserService {
    User createUser(String email, String password, UserRole role);
    List<User> getAllUsers();
    void deactivateUser(String id);
    void reactivateUser(String id);
    void changePassword(String id, String newPassword);
    void setUserOnline(String userId);
    String getUserStatus(String userId);
    void setUserOffline(String userId); // Added for logout 
    void updateExistingUsersLastActive(); // Added to update lastActive for existing users
}