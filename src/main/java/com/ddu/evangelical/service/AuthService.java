package com.ddu.evangelical.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ddu.evangelical.model.User;
import com.ddu.evangelical.repository.UserRepository;
import com.ddu.evangelical.service.email.EmailService;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public User login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        // Note: In production, you should use a password encoder (like BCrypt) instead of .equals()
        if (user.isPresent() && user.get().getPassword().equals(password)) return user.get();
        return null;
    }

    // UPDATED: Force self-registering users to be MEMBERS
    public User register(User user) {
        // Security check: Prevent users from making themselves ADMIN during self-registration
        user.setRole("MEMBER"); 
        return userRepository.save(user);
    }

    public User createMember(String email, String firstName, String lastName, String phone, String role) {
        Optional<User> existing = userRepository.findByEmail(email);
        if (existing.isPresent()) {
            throw new RuntimeException("Email already exists: " + email);
        }

        User user = new User();
        user.setUsername(generateUsername(firstName, lastName));
        String rawPassword = generatePassword();
        user.setPassword(rawPassword);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        
        // UPDATED: Safely validate the role passed by the Admin
        String assignedRole = "MEMBER"; // Default
        if (role != null && (role.equalsIgnoreCase("ADMIN") || role.equalsIgnoreCase("MEMBER"))) {
            assignedRole = role.toUpperCase();
        }
        user.setRole(assignedRole);

        User saved = userRepository.save(user);

        String subject = "Welcome to DDU Evangelical Team";
        String body =
            "Dear " + firstName + ",\n\n" +
            "Your account has been created!\n\n" +
            "????????????????????????????????????\n" +
            "  YOUR LOGIN CREDENTIALS\n" +
            "????????????????????????????????????\n\n" +
            "Username: " + saved.getUsername() + "\n" +
            "Password: " + rawPassword + "\n\n" +
            "????????????????????????????????????\n\n" +
            "Login at: http://localhost:3000/login\n\n" +
            "Please change your password after first login.\n\n" +
            "Welcome to the family!\n\n" +
            "God bless you,\n" +
            "DDU Evangelical Team";

        try {
            emailService.sendEmail(email, subject, body);
        } catch (Exception e) {
            System.out.println("Email failed: " + e.getMessage());
        }

        return saved;
    }

    private String generateUsername(String firstName, String lastName) {
        String base = (firstName + lastName).toLowerCase().replaceAll("[^a-z0-9]", "");
        if (base.isEmpty()) base = "member";
        String username = base;
        int suffix = 1;
        while (userRepository.findByUsername(username).isPresent()) {
            username = base + suffix;
            suffix++;
        }
        return username;
    }

    private String generatePassword() {
        String chars = "ABCDEFGHJKMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}