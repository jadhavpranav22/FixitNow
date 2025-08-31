package com.household_repair.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.household_repair.Config.JwtUtil;
import com.household_repair.dto.AuthResponse;
import com.household_repair.dto.LoginRequest;
import com.household_repair.dto.RegisterRequest;
import com.household_repair.entity.Users;
import com.household_repair.repo.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MailService mailService;

    // ✅ Register new user
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already registered with this email");
        }

        Users user = new Users();
        user.setUserName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return new AuthResponse(token, user.getRole());
    }

    // ✅ Login
    public AuthResponse login(LoginRequest request) {
        Users user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return new AuthResponse(token, user.getRole());
    }

    // ✅ Forgot password - Send reset email with token
    public void sendResetPasswordEmail(String email) {
        Optional<Users> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("No user registered with this email");
        }

        Users user = userOpt.get();

        // Generate and store token
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        userRepository.save(user);

        // Send reset email
        String resetLink = "http://localhost:3000/reset-password?token=" + token;
        System.out.println("📧 Sending reset email to: " + user.getEmail());
        System.out.println("🔗 Reset link: " + resetLink);
        mailService.sendResetPasswordEmail(
            user.getEmail(),
            "Reset Your Password",
            "Click the link to reset your password:\n\n" + resetLink
        );
    }

    // ✅ Reset password using token
    public void resetPassword(String token, String newPassword) {
        if (token == null || token.isBlank()) {
            System.out.println("❌ Token is missing.");
            throw new IllegalArgumentException("Token is required.");
        }

        List<Users> users = userRepository.findByResetToken(token);
        System.out.println("🔍 Matching users for token '" + token + "': " + users.size());

        if (users.isEmpty()) {
            throw new IllegalArgumentException("Invalid or expired reset token.");
        } else if (users.size() > 1) {
            throw new IllegalStateException("Token conflict: multiple users found.");
        }

        Users user = users.get(0);
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null); // clear token
        userRepository.save(user);

        System.out.println("✅ Password reset successful for: " + user.getEmail());
    }

}
