package com.example.backend.controllers;

import com.example.backend.models.CreateUserRequest;
import com.example.backend.models.User;
import com.example.backend.models.UserDto;
import com.example.backend.services.UserService;
import com.example.backend.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        try {
            if (request == null || request.getEmail() == null || request.getPassword() == null || request.getRole() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            User user = userService.createUser(request.getEmail(), request.getPassword(), request.getRole());
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            List<UserDto> userDtos = users.stream().map(user -> {
                UserDto dto = new UserDto(user);
                dto.setStatus(user.getStatus()); // Directly use the status field ("ONLINE", "OFFLINE", "DEACTIVATED")
                return dto;
            }).collect(Collectors.toList());
            return ResponseEntity.ok(userDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Map<String, String>> deactivateUser(@PathVariable String id) {
        try {
            userService.deactivateUser(id);
            return ResponseEntity.ok(Map.of("message", "User deactivated successfully."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Failed to deactivate user."));
        }
    }

    @PostMapping("/{id}/reactivate")
    public ResponseEntity<Map<String, String>> reactivateUser(@PathVariable String id) {
        try {
            userService.reactivateUser(id);
            return ResponseEntity.ok(Map.of("message", "User reactivated successfully."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Failed to reactivate user."));
        }
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Map<String, String>> changePassword(@PathVariable String id, @RequestBody Map<String, String> request) {
        try {
            String newPassword = request.get("password");
            if (newPassword == null || newPassword.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "New password is required."));
            }
            userService.changePassword(id, newPassword);
            return ResponseEntity.ok(Map.of("message", "Password updated successfully."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Failed to change password."));
        }
    }

    @PostMapping("/{id}/set-online")
    public ResponseEntity<Map<String, String>> setUserOnline(@PathVariable String id) {
        try {
            userService.setUserOnline(id);
            return ResponseEntity.ok(Map.of("message", "User set to online"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Failed to set user online"));
        }
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<Map<String, String>> getUserStatus(@PathVariable String id) {
        try {
            String status = userService.getUserStatus(id);
            return ResponseEntity.ok(Map.of("status", status));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Failed to get user status"));
        }
    }

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
                SecurityContextHolder.clearContext(); // Clear authentication context
                return ResponseEntity.ok(Map.of("message", "User logged out successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid token"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Failed to logout: " + e.getMessage()));
        }
    }

    @PostMapping("/update-last-active")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Map<String, String>> updateLastActive() {
        try {
            userService.updateExistingUsersLastActive();
            return ResponseEntity.ok(Map.of("message", "Updated lastActive for all existing users."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Failed to update lastActive: " + e.getMessage()));
        }
    }
}