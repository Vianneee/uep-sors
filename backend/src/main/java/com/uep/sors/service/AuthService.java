package com.uep.sors.service;

import com.uep.sors.dto.*;
import com.uep.sors.entity.User;
import com.uep.sors.repository.UserRepository;
import com.uep.sors.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByStudentId(request.getStudentId())) {
            throw new RuntimeException("Student ID already registered");
        }
        User user = User.builder()
            .username(request.getUsername())
            .studentId(request.getStudentId())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(request.getRole())
            .build();
        userRepository.save(user);
        return new AuthResponse(
            jwtService.generateToken(user),
            user.getRole().name(),
            user.getUsername()
        );
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByStudentId(request.getStudentId())
            .orElseThrow(() -> new RuntimeException("Student ID not found"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }
        return new AuthResponse(
            jwtService.generateToken(user),
            user.getRole().name(),
            user.getUsername()
        );
    }
}