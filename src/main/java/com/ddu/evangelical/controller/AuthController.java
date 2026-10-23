package com.ddu.evangelical.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.ddu.evangelical.service.AuthService;
import com.ddu.evangelical.model.User;
import com.ddu.evangelical.dto.LoginRequest;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public User login(@RequestBody LoginRequest request) {
        return authService.login(request.getUsername(), request.getPassword());
    }

    // 1. SELF REGISTRATION (Forced MEMBER role)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            // Force the role to MEMBER so self-registering users cannot create Admin accounts
            user.setRole("MEMBER"); 
            User registeredUser = authService.register(user);
            return ResponseEntity.ok(registeredUser);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // 2. ADMIN CREATION (Admin can specify role)
    @PostMapping("/create-member")
    public ResponseEntity<?> createMember(@RequestBody Map<String, String> body) {
        try {
            // Note: In a production app, you MUST secure this endpoint using Spring Security 
            // (e.g., @PreAuthorize("hasRole('ADMIN')")) so only admins can access it.
            
            User user = authService.createMember(
                body.get("email"),
                body.get("firstName"),
                body.get("lastName"),
                body.get("phone"),
                body.getOrDefault("role", "MEMBER") // Uses the role provided by the Admin
            );
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}