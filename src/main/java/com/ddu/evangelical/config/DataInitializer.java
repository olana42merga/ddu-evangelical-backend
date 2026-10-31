package com.ddu.evangelical.config;

import com.ddu.evangelical.model.User;
import com.ddu.evangelical.model.Post;
import com.ddu.evangelical.repository.UserRepository;
import com.ddu.evangelical.repository.PostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PostRepository postRepository) {
        return args -> {

            // 1. Create Admin User (Plain text password, as it was working before)
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setFirstName("System");
                admin.setLastName("Admin");
                admin.setEmail("admin@ddu.com");
                admin.setUsername("admin");
                admin.setPassword("admin123");
                admin.setRole("ADMIN");
                userRepository.save(admin);
                System.out.println("Live Admin User Created: admin / admin123");
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