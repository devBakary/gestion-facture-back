package com.gef.gest.Service.config;

import com.gef.gest.Model.User;
import com.gef.gest.Repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UserRepository repo, PasswordEncoder encoder) {
        return args -> {

            if (repo.findByUsername("admin").isEmpty()) {

                User user = new User();
                user.setUsername("admin");
                user.setPassword(encoder.encode("1234"));
                user.setRole("ADMIN");

                repo.save(user);

                System.out.println("✅ USER ADMIN CRÉÉ");
            }
        };
    }
}