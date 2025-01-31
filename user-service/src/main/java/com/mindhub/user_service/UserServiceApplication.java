package com.mindhub.user_service;

import com.mindhub.user_service.models.RoleType;
import com.mindhub.user_service.models.UserEntity;
import com.mindhub.user_service.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            userRepository.save(new UserEntity("admin1@email.com", passwordEncoder.encode("password123"), "admin1", RoleType.ADMIN));
        };
    }
}
