package com.household_repair.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.household_repair.entity.Users;
import com.household_repair.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class AdminInitializer {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void createAdminIfNotExists() {
        userRepository.findByEmail("mayurmali161616@gmail.com").ifPresentOrElse(user -> {
            user.setPassword(passwordEncoder.encode("Mayur@1624"));
            user.setRole("ADMIN"); 
            userRepository.save(user);
            System.out.println("✅ Admin credentials updated.");
        }, () -> {
            Users admin = new Users();
            admin.setUserName("Mayur");
            admin.setEmail("mayurmali161616@gmail.com");
            admin.setPassword(passwordEncoder.encode("Mayur@1624"));
            admin.setRole("ADMIN");
            userRepository.save(admin);
            System.out.println("✅ Admin user created.");
        });
    }
}
