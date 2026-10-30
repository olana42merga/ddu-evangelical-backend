package com.ddu.evangelical.config;

import com.ddu.evangelical.model.User;
import com.ddu.evangelical.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            // Check if admin exists to prevent duplicates on restart
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setFirstName("System");
                admin.setLastName("Admin");
                admin.setEmail("admin@ddu.com");
                admin.setUsername("admin");

                // Note: Since we haven't set up BCrypt encryption yet, this is plain text.
                admin.setPassword("admin123");
                admin.setRole("ADMIN");

                userRepository.save(admin);
                System.out.println("[OK] Live Admin User Created: admin / admin123");
            }
        };
    }
}