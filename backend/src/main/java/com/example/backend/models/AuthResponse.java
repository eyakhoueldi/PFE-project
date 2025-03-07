package com.example.backend.models;


public class AuthResponse {
    private String token;
    private String email;

    // Constructor with token and email
    public AuthResponse(String token, String email) {
        this.token = token;
        this.email = email;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}