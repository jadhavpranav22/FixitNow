package com.household_repair.controller;

import com.household_repair.Config.JwtUtil;
import com.household_repair.dto.LoginRequest;
import com.household_repair.dto.LoginResponse;
import com.household_repair.dto.RegisterRequest;
import com.household_repair.entity.Users;
import com.household_repair.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            Users user = authService.register(request);
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
            String role = user.getRole();
            if (role.startsWith("ROLE_")) role = role.substring(5);  // Remove ROLE_ prefix before sending
            return ResponseEntity.ok(new LoginResponse(token, user.getUsername(), role, user.getUserId()));
        } catch (RuntimeException e) {
            logger.error("Registration failed: ", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during registration: ", e);
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Users user = authService.login(request.getEmail(), request.getPassword());
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
            String role = user.getRole();
            if (role.startsWith("ROLE_")) role = role.substring(5);  // Remove ROLE_ prefix before sending
            return ResponseEntity.ok(new LoginResponse(token, user.getUsername(), role, user.getUserId()));
        } catch (RuntimeException e) {
            logger.error("Login failed: ", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during login: ", e);
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> testAuth() {
        return ResponseEntity.ok("Authentication test endpoint is working");
    }
}
