package com.uep.sors.service;

import com.uep.sors.dto.*;
import com.uep.sors.entity.OtpCode;
import com.uep.sors.entity.OtpCode.OtpType;
import com.uep.sors.entity.Role;
import com.uep.sors.entity.User;
import com.uep.sors.repository.OtpRepository;
import com.uep.sors.repository.UserRepository;
import com.uep.sors.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    // ─── Password Validation ───────────────────────────────
    private void validatePassword(String password) {
        if (password.length() < 8)
            throw new RuntimeException("Password must be at least 8 characters.");
        if (!password.matches(".*[A-Z].*"))
            throw new RuntimeException("Password must have at least one uppercase letter.");
        if (!password.matches(".*[0-9].*"))
            throw new RuntimeException("Password must have at least one number.");
        if (!password.matches(".*[^A-Za-z0-9].*"))
            throw new RuntimeException("Password must have at least one special character.");
    }

    // ─── OTP Generator ─────────────────────────────────────
    private String generateOTP() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    // ─── Save OTP to DB ────────────────────────────────────
    private void saveAndSendOTP(String email, OtpType type) {
        // Invalidate old OTPs
        otpRepository.invalidateAll(email, type);

        String code = generateOTP();

        OtpCode otp = new OtpCode();
        otp.setEmail(email);
        otp.setCode(code);
        otp.setType(type);
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        otp.setUsed(false);
        otpRepository.save(otp);

        try {
            emailService.sendOTP(email, code, type.name());
        } catch (Exception e) {
            throw new RuntimeException("Failed to send OTP email. Please try again.");
        }
    }

    // ─── REGISTER Step 1 ───────────────────────────────────
    public String register(RegisterRequest request) {
        // Duplicate checks
        if (userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("Email is already registered.");
        if (userRepository.existsByStudentId(request.getStudentId()))
            throw new RuntimeException("Student ID is already registered.");

        // Password match
        if (!request.getPassword().equals(request.getConfirmPassword()))
            throw new RuntimeException("Passwords do not match.");

        // Password strength
        validatePassword(request.getPassword());

        // Age and year level range
        if (request.getAge() < 16 || request.getAge() > 60)
            throw new RuntimeException("Age must be between 16 and 60.");
        if (request.getYearLevel() < 1 || request.getYearLevel() > 6)
            throw new RuntimeException("Year level must be between 1 and 6.");

        // Save user (unverified)
        User user = new User();
        user.setFullName(request.getFullName());
        user.setStudentId(request.getStudentId());
        user.setAge(request.getAge());
        user.setProgram(request.getProgram());
        user.setYearLevel(request.getYearLevel());
        user.setEmail(request.getEmail().toLowerCase().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.STUDENT);
        user.setIsVerified(false);
        userRepository.save(user);

        // Send OTP
        saveAndSendOTP(request.getEmail().toLowerCase().trim(), OtpType.REGISTER);

        return "Registration successful! Check your email for the verification code.";
    }

    // ─── REGISTER Step 2 — Verify OTP ──────────────────────
    public String verifyRegister(VerifyOtpRequest request) {
        OtpCode otp = otpRepository
                .findLatestValid(request.getEmail().toLowerCase().trim(), OtpType.REGISTER)
                .orElseThrow(() -> new RuntimeException("Invalid or expired OTP."));

        if (!otp.getCode().equals(request.getOtp()))
            throw new RuntimeException("Invalid or expired OTP.");

        otp.setUsed(true);
        otpRepository.save(otp);

        User user = userRepository.findByEmail(request.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new RuntimeException("User not found."));
        user.setIsVerified(true);
        userRepository.save(user);

        return "Email verified! You can now log in.";
    }

    // ─── LOGIN Step 1 ──────────────────────────────────────
    public Map<String, String> login(LoginRequest request) {
        User user;

        // Allow login by student ID or email
        if (request.getEmail().contains("@")) {
            user = userRepository.findByEmail(request.getEmail().toLowerCase().trim())
                    .orElseThrow(() -> new RuntimeException("Invalid credentials."));
        } else {
            user = userRepository.findByStudentId(request.getEmail().trim())
                    .orElseThrow(() -> new RuntimeException("Invalid credentials."));
        }

        if (!user.getIsVerified())
            throw new RuntimeException("Please verify your email first.");

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new RuntimeException("Invalid credentials.");

        // Skip OTP for admin — return token directly
        if (user.getRole() == Role.ADMIN) {
            String token = jwtService.generateToken(user.getEmail(), user.getRole().name(), user.getOrganizationId());
            return Map.of(
                "message", "Admin login successful.",
                "email", user.getEmail(),
                "isAdmin", "true",
                "token", token
            );
        }

        saveAndSendOTP(user.getEmail(), OtpType.LOGIN);
        return Map.of("message", "Credentials verified. OTP sent to your email.", "email", user.getEmail());
    }

    // ─── LOGIN Step 2 — Verify OTP ─────────────────────────
    public AuthResponse verifyLogin(VerifyOtpRequest request) {
        OtpCode otp = otpRepository
                .findLatestValid(request.getEmail().toLowerCase().trim(), OtpType.LOGIN)
                .orElseThrow(() -> new RuntimeException("Invalid or expired OTP."));

        if (!otp.getCode().equals(request.getOtp()))
            throw new RuntimeException("Invalid or expired OTP.");

        otp.setUsed(true);
        otpRepository.save(otp);

        User user = userRepository.findByEmail(request.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new RuntimeException("User not found."));

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name(), user.getOrganizationId());

        return new AuthResponse(
                token,
                user.getRole().name(),
                user.getFullName(),
                user.getEmail(),
                user.getStudentId(),
                user.getOrganizationId()
        );
    }

    // ─── GUEST Login ───────────────────────────────────────
    public AuthResponse guestLogin() {
        String token = jwtService.generateToken("guest", Role.GUEST.name());

        return new AuthResponse(
                token,
                Role.GUEST.name(),
                "Guest",
                null,
                null,
                null
        );
    }

    // ─── RESEND OTP ────────────────────────────────────────
    public String resendOtp(String email, String type) {
        OtpType otpType;
        try {
            otpType = OtpType.valueOf(type.toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Invalid OTP type. Must be LOGIN or REGISTER.");
        }

        userRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(() -> new RuntimeException("No account found with that email."));

        saveAndSendOTP(email.toLowerCase().trim(), otpType);

        return "OTP resent. Check your email.";
    }

    // ─── CREATE PIO (Editor in Chief only) ─────────────────
    public String createPio(CreatePioRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("Email is already registered.");
        if (userRepository.existsByStudentId(request.getStudentId()))
            throw new RuntimeException("Student ID is already registered.");

        validatePassword(request.getPassword());

        User pio = new User();
        pio.setFullName(request.getFullName());
        pio.setStudentId(request.getStudentId());
        pio.setAge(request.getAge());
        pio.setProgram(request.getProgram());
        pio.setYearLevel(request.getYearLevel());
        pio.setEmail(request.getEmail().toLowerCase().trim());
        pio.setPassword(passwordEncoder.encode(request.getPassword()));
        pio.setRole(Role.PIO);
        pio.setOrganizationId(request.getOrganizationId());
        pio.setIsVerified(true);
        userRepository.save(pio);

        return "PIO account created successfully.";
    }
}