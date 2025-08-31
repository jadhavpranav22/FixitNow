package com.household_repair.controller;

import com.household_repair.dto.AuthResponse;
import com.household_repair.dto.LoginRequest;
import com.household_repair.dto.RegisterRequest;
import com.household_repair.entity.Users;
import com.household_repair.repo.UserRepository;
import com.household_repair.service.AuthService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ Register
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    // ✅ Login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    // ✅ Forgot Password - send reset email
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            authService.sendResetPasswordEmail(email);
            return ResponseEntity.ok("Reset password link sent successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error: Try again later.");
        }
    }

    // ✅ Reset Password - update new password
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            String newPassword = request.get("password");

            authService.resetPassword(token, newPassword);
            return ResponseEntity.ok("Password reset successful.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body("Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();  // show exact issue in logs
            return ResponseEntity.status(500).body("Server error: Try again.");
        }
    }


}
