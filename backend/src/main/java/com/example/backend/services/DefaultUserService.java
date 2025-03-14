package com.example.backend.services;

import com.example.backend.models.User;
import com.example.backend.models.UserRole;
import com.example.backend.models.Administrator;
import com.example.backend.models.ProjectManager;
import com.example.backend.models.DevTeamMember;
import com.example.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DefaultUserService implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User createUser(String email, String password, UserRole role) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User with email " + email + " already exists.");
        }

        User user;
        switch (role) {
            case ADMINISTRATOR:
                user = new Administrator();
                break;
            case PROJECT_MANAGER:
                user = new ProjectManager();
                break;
            case DEV_TEAM_MEMBER:
                user = new DevTeamMember();
                break;
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }

        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setStatus("OFFLINE");

        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deactivateUser(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        userOptional.ifPresentOrElse(
            user -> {
                user.setStatus("DEACTIVATED");
                userRepository.save(user);
            },
            () -> { throw new IllegalArgumentException("User with ID " + id + " not found."); }
        );
    }

    @Override
    public void reactivateUser(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        userOptional.ifPresentOrElse(
            user -> {
                if (!"DEACTIVATED".equals(user.getStatus())) {
                    throw new IllegalArgumentException("User with ID " + id + " is not deactivated.");
                }
                user.setStatus("OFFLINE"); // Set to OFFLINE, not ONLINE, until user logs in
                userRepository.save(user);
            },
            () -> { throw new IllegalArgumentException("User with ID " + id + " not found."); }
        );
    }

    @Override
    public void changePassword(String id, String newPassword) {
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("New password cannot be empty.");
        }
        Optional<User> userOptional = userRepository.findById(id);
        userOptional.ifPresentOrElse(
            user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
            },
            () -> { throw new IllegalArgumentException("User with ID " + id + " not found."); }
        );
    }

    @Override
    public void setUserOnline(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        userOptional.ifPresent(user -> {
            if (!"DEACTIVATED".equals(user.getStatus())) {
                user.setStatus("ONLINE");
                user.setLastActive(new Date());
                userRepository.save(user);
            }
        });
    }

    @Override
    public String getUserStatus(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.map(User::getStatus).orElse("OFFLINE");
    }

    @Override
    public void setUserOffline(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        userOptional.ifPresent(user -> {
            if (!"DEACTIVATED".equals(user.getStatus())) {
                user.setStatus("OFFLINE");
                user.setLastActive(new Date(0));
                System.out.println("User " + user.getEmail() + " set offline. Status: " + user.getStatus() + ", Last Active: " + user.getLastActive());
                userRepository.save(user);
            }
        });
    }

    @Override
    public void updateExistingUsersLastActive() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (!"DEACTIVATED".equals(user.getStatus()) && !user.isOnline()) {
                user.setLastActive(new Date(0));
                user.setStatus("OFFLINE");
                userRepository.save(user);
                System.out.println("Updated user " + user.getEmail() + " lastActive to epoch time.");
            }
        }
    }
}