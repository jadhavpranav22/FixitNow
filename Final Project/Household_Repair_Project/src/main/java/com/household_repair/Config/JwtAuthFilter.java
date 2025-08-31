package com.household_repair.Config;

import com.household_repair.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        
        logger.info("Processing request: {} {} with Authorization header: {}", 
                    method, requestURI, request.getHeader("Authorization") != null ? "present" : "missing");

        try {
            String authHeader = request.getHeader("Authorization");
            String token = null;
            String username = null;

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                try {
                    username = jwtUtil.extractUsername(token);
                    logger.debug("Extracted username from token: {}", username);
                } catch (Exception e) {
                    logger.warn("Failed to extract username from token: {}", e.getMessage());
                }
            } else {
                logger.debug("No Bearer token found in Authorization header for path: {}", requestURI);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                    logger.debug("Loaded UserDetails for username: {} with authorities: {}", 
                                username, userDetails.getAuthorities());

                    if (jwtUtil.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        logger.info("User authenticated successfully: {} with roles: {} for request: {}", 
                                  username, userDetails.getAuthorities(), requestURI);
                    } else {
                        logger.warn("JWT token validation failed for user: {} for request: {}", username, requestURI);
                    }
                } catch (Exception e) {
                    logger.error("Error loading user details for username: {}", username, e);
                }
            } else if (username == null && authHeader != null && authHeader.startsWith("Bearer ")) {
                logger.warn("Could not extract username from token for request: {}", requestURI);
            }
        } catch (Exception e) {
            logger.error("Could not set user authentication in security context for request: {}", requestURI, e);
        }

        // Log the final authentication state
        var auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Final authentication state for {} {}: authenticated={}, name={}, authorities={}", 
                   method, requestURI, 
                   auth != null ? auth.isAuthenticated() : "null",
                   auth != null ? auth.getName() : "null",
                   auth != null ? auth.getAuthorities() : "null");

        logger.info("Continuing filter chain for request: {} {}", method, requestURI);
        filterChain.doFilter(request, response);
    }
}
