package com.example.backend.security;

public class DeactivatedAccountException extends RuntimeException {
    public DeactivatedAccountException(String message) {
        super(message);
    }
}