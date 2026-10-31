package com.ddu.evangelical.config;

import com.ddu.evangelical.model.User;
import com.ddu.evangelical.model.Post; // ADDED
import com.ddu.evangelical.repository.UserRepository;
import com.ddu.evangelical.repository.PostRepository; // ADDED
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder; // ADDED

@Configuration
public class DataInitializer {

    // ADDED PostRepository and PasswordEncoder to the parameters
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PostRepository postRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {

            // 1. Create Admin User
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setFirstName("System");
                admin.setLastName("Admin");
                admin.setEmail("admin@ddu.com");
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123")); // Encrypted password
                admin.setRole("ADMIN");
                userRepository.save(admin);
                System.out.println("Live Admin User Created securely!");
            }

            // 2. Create a Test Post if the database is empty
            if (postRepository.count() == 0) {
                Post post = new Post();
                post.setTitle("Welcome to FOCUS DDU!");
                post.setContent(
                        "This is our very first post on the live website. We are excited to share the love of Christ with everyone on campus.");
                post.setImagePath("");
                postRepository.save(post);
                System.out.println("Live Test Post Created successfully!");
            }

        };
    }
}