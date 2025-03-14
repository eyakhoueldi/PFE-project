package com.example.backend.repositories;

import com.example.backend.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email); // Already present, used for login

    // Optional: Add a method to find users by role if needed for role-based operations
    Optional<User> findByEmailAndRole(String email, String role);
}