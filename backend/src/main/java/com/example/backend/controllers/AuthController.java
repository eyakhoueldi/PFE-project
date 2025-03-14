package com.example.backend.controllers;

import com.example.backend.models.AuthRequest;
import com.example.backend.models.AuthResponse;
import com.example.backend.models.User;
import com.example.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.backend.security.JwtUtils;
import com.example.backend.security.UserDetailsImpl;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200") // Allow Angular frontend
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService; // Added to handle user status updates

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest authRequest) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

        // Set authentication in SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Extract user details
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String email = userDetails.getUsername(); // Use getUsername() for email
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .orElse("UNKNOWN"); // Extract role from authorities

        // Generate JWT token with email and role
        String jwt = jwtUtils.generateJwtToken(email, role);

        // Set user status to ONLINE
        User user = userService.getAllUsers().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userService.setUserOnline(user.getId());

        // Build response with email and role
        AuthResponse authResponse = new AuthResponse(jwt, email, role);
        return ResponseEntity.ok(authResponse);
    }

    // New logout endpoint
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String jwt = authorizationHeader.substring(7); // Remove "Bearer " prefix
            if (jwtUtils.validateJwtToken(jwt)) {
                String email = jwtUtils.getClaimsFromJwt(jwt).getSubject();
                User user = userService.getAllUsers().stream()
                        .filter(u -> u.getEmail().equals(email))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));
                userService.setUserOffline(user.getId());
                return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
            } else {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Logout failed: " + e.getMessage()));
        }
    }
}