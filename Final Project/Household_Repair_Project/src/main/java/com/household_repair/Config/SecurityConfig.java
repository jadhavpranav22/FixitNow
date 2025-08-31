package com.household_repair.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/api/services/**").permitAll()
                .requestMatchers("/services/**").permitAll()
                .requestMatchers("/subservices/**").permitAll()
                .requestMatchers("/bookings/test").permitAll()
                .requestMatchers("/bookings/auth-test").permitAll()
                .requestMatchers("/bookings/debug").permitAll()
                .requestMatchers("/bookings/test-create").permitAll()
                .requestMatchers("/bookings/token-test").permitAll()
                .requestMatchers("/bookings/open").permitAll()
                .requestMatchers("/bookings/comprehensive-test").permitAll()
                .requestMatchers("/bookings/pending").hasAnyAuthority("ROLE_CONTRACTOR", "ROLE_ADMIN")
                .requestMatchers("/bookings/contractor/**").hasAnyAuthority("ROLE_CONTRACTOR", "ROLE_ADMIN")
                .requestMatchers("/bookings/customer/**").hasAnyAuthority("ROLE_CUSTOMER", "ROLE_ADMIN")
                .requestMatchers("/bookings").hasAnyAuthority("ROLE_CUSTOMER", "ROLE_ADMIN")
                .requestMatchers("/bookings/*/accept").hasAnyAuthority("ROLE_CONTRACTOR", "ROLE_ADMIN")
                .requestMatchers("/bookings/*/reject").hasAnyAuthority("ROLE_CONTRACTOR", "ROLE_ADMIN")
                .requestMatchers("/bookings/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/user/**").hasAuthority("ROLE_CUSTOMER")
                .requestMatchers("/provider/**").hasAuthority("ROLE_CONTRACTOR")
                .requestMatchers("/contractor/**").hasAuthority("ROLE_CONTRACTOR")
                .requestMatchers("/customer/**").hasAuthority("ROLE_CUSTOMER")
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Add JWT filter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*")); // Allow all origins for testing
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(false); // Set to false when using allowedOrigins("*")

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
