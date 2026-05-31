hcontroller · JAVA
package com.uep.sors.controller;
 
import com.uep.sors.dto.*;
import com.uep.sors.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
 
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService authService;
 
    // ─── Register Step 1 ───────────────────────────────────
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            String message = authService.register(request);
            return ResponseEntity.ok(Map.of("success", true, "message", message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
 
    // ─── Register Step 2 — Verify OTP ──────────────────────
    @PostMapping("/verify-register")
    public ResponseEntity<?> verifyRegister(@RequestBody VerifyOtpRequest request) {
        try {
            String message = authService.verifyRegister(request);
            return ResponseEntity.ok(Map.of("success", true, "message", message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
 
    // ─── Login Step 1 ──────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Map<String, String> result = authService.login(request);
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("success", true);
            response.put("message", result.get("message"));
            response.put("email", result.get("email"));
            if (result.containsKey("isAdmin")) {
                response.put("isAdmin", result.get("isAdmin"));
                response.put("token", result.get("token"));
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
 
    // ─── Login Step 2 — Verify OTP ─────────────────────────
    @PostMapping("/verify-login")
    public ResponseEntity<?> verifyLogin(@RequestBody VerifyOtpRequest request) {
        try {
            AuthResponse response = authService.verifyLogin(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
 
    // ─── Guest Login ───────────────────────────────────────
    @PostMapping("/guest")
    public ResponseEntity<?> guestLogin() {
        try {
            AuthResponse response = authService.guestLogin();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
 
    // ─── Resend OTP ────────────────────────────────────────
    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestBody Map<String, String> body) {
        try {
            String message = authService.resendOtp(body.get("email"), body.get("type"));
            return ResponseEntity.ok(Map.of("success", true, "message", message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
 
    // ─── Create PIO (Editor in Chief only) ─────────────────
    @PostMapping("/create-pio")
    @PreAuthorize("hasRole('EDITOR_IN_CHIEF')")
    public ResponseEntity<?> createPio(@RequestBody CreatePioRequest request) {
        try {
            String message = authService.createPio(request);
            return ResponseEntity.ok(Map.of("success", true, "message", message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}