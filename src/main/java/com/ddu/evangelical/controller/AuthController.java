package com.ddu.evangelical.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.ddu.evangelical.service.AuthService;
import com.ddu.evangelical.model.User;
import com.ddu.evangelical.dto.LoginRequest;
import com.ddu.evangelical.service.email.EmailService; // ADDED
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Changed to * for Vercel/Netlify
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailService emailService; // ADDED

    @PostMapping("/login")
    public User login(@RequestBody LoginRequest request) {
        return authService.login(request.getUsername(), request.getPassword());
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            user.setRole("MEMBER");
            User registeredUser = authService.register(user);

            // SEND WELCOME EMAIL
            try {
                emailService.sendEmail(
                        registeredUser.getEmail(),
                        "Welcome to FOCUS DDU Evangelical Team!",
                        "Hello " + registeredUser.getFirstName()
                                + ",\n\nWelcome to the team! Your account has been created successfully.");
            } catch (Exception e) {
                System.out.println("Email failed: " + e.getMessage());
            }

            return ResponseEntity.ok(registeredUser);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/create-member")
    public ResponseEntity<?> createMember(@RequestBody Map<String, String> body) {
        try {
            User user = authService.createMember(
                    body.get("email"),
                    body.get("firstName"),
                    body.get("lastName"),
                    body.get("phone"),
                    body.getOrDefault("role", "MEMBER"));

            // SEND ACCOUNT CREATION EMAIL
            try {
                emailService.sendEmail(
                        user.getEmail(),
                        "Your DDU Evangelical Team Account",
                        "Hello " + user.getFirstName()
                                + ",\n\nAn account has been created for you by the Admin.\nUsername: "
                                + user.getUsername() + "\nPassword: " + user.getPassword());
            } catch (Exception e) {
                System.out.println("Email failed: " + e.getMessage());
            }

            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}