package com.example.backend.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "User")
public class Administrator extends User {
    public Administrator() {
        super();
        setRole(UserRole.ADMINISTRATOR);
    }

    public Administrator(String name, String password, String email) {
        super(name, password, email, UserRole.ADMINISTRATOR);
    }

    public void createUser(User user) {
        System.out.println("Administrator " + getName() + " is creating a new user: " + user.getName());
    }

    public void deleteUser(User user) {
        System.out.println("Administrator " + getName() + " is deleting user: " + user.getName());
    }
}