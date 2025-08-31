package com.household_repair.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Role is required") // USER / PROVIDER
    private String role;

    public RegisterRequest() {}

    public RegisterRequest(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        setRole(role); // Use setter to apply validation
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }

    public void setRole(String role) {
        if (!"USER".equalsIgnoreCase(role) && !"PROVIDER".equalsIgnoreCase(role)) {
            throw new IllegalArgumentException("Only USER or PROVIDER roles are allowed");
        }
        this.role = role.toUpperCase(); // Normalize role input
    }
}
