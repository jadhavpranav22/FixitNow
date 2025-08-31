package com.household_repair.service;

import com.household_repair.dto.RegisterRequest;
import com.household_repair.entity.Users;
import com.household_repair.repo.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ContractorService contractorService;

    @Transactional
    public Users register(RegisterRequest req) {
        String emailLower = req.getEmail().trim().toLowerCase();

        if (userRepo.findByEmailIgnoreCase(emailLower).isPresent()) {
            throw new RuntimeException("User is already registered.");
        }

        Users user = new Users();
        user.setUsername(req.getName().trim());
        user.setEmail(emailLower);
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        // Normalize role format
        String role = req.getRole().toUpperCase();
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }
        user.setRole(role);

        // Save user first
        Users savedUser = userRepo.save(user);

        // Create corresponding customer or contractor record
        String roleWithoutPrefix = role.replace("ROLE_", "");
        if ("CUSTOMER".equals(roleWithoutPrefix)) {
            customerService.registerCustomerWithUser(savedUser, req.getName());
            System.out.println("Created customer record for: " + savedUser.getEmail());
        } else if ("CONTRACTOR".equals(roleWithoutPrefix)) {
            contractorService.registerContractorWithUser(savedUser, req.getName());
            System.out.println("Created contractor record for: " + savedUser.getEmail());
        }

        return savedUser;
    }

    public Users login(String email, String password) {
        String emailLower = email.trim().toLowerCase();

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(emailLower, password)
            );
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid email or password");
        }

        return userRepo.findByEmailIgnoreCase(emailLower)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
