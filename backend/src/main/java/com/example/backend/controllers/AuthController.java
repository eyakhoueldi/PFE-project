package com.example.backend.controllers;

import com.example.backend.models.AuthRequest;
import com.example.backend.models.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.backend.security.JwtUtils;
import com.example.backend.security.UserDetailsImpl;

@CrossOrigin(origins = "http://localhost:4200") // Allow Angular frontend
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest authRequest) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

        // Set authentication in SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Extract user details
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Generate JWT token using email
        String jwt = jwtUtils.generateJwtToken(userDetails.getUsername()); // Use getUsername()

        // Build response
        AuthResponse authResponse = new AuthResponse(jwt, userDetails.getUsername()); // Use getUsername()
        return ResponseEntity.ok(authResponse);
    }
}