package com.household_repair.dto;

public class AuthResponse {
    private String token;
    private String role;
    private Long userId;

    public AuthResponse() {}
    public AuthResponse(String token, String role, Long userId) {
        this.token = token; this.role = role; this.userId = userId;
    }
    public String getToken() { return token; }
    public void setToken(String t) { token = t; }
    public String getRole() { return role; }
    public void setRole(String r) { role = r; }
    public Long getUserId() { return userId; }
    public void setUserId(Long id) { userId = id; }
}
