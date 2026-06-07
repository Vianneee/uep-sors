package com.uep.sors.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth

                // ─── Allow CORS preflight for all routes ───
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // ─── Public endpoints (no token needed) ───
                .requestMatchers(
                 "/api/auth/register",
                 "/api/auth/verify-register",
                 "/api/auth/login",
                "/api/auth/verify-login",
                "/api/auth/guest",
                "/api/auth/resend-otp",
                "/api/health",
                "/api/organizations/**",
                 "/h2-console/**"
                ).permitAll()

                // ─── Guest can only view ───────────────────
                .requestMatchers(
                    "/organizations/**",
                    "/events/**",
                    "/regulations/**"
                ).hasAnyRole("GUEST", "STUDENT", "ADMIN", "PIO", "EDITOR_IN_CHIEF")

                // ─── Students and above only ───────────────
                .requestMatchers(
                    "/apply/**",
                    "/dashboard/**"
                ).hasAnyRole("STUDENT", "ADMIN", "PIO", "EDITOR_IN_CHIEF")

                // ─── Editor in Chief only ──────────────────
                .requestMatchers("/api/auth/create-pio")
                    .hasRole("EDITOR_IN_CHIEF")

                // ─── Everything else needs authentication ──
                .anyRequest().authenticated()
            )
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}