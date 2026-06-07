package com.uep.sors.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${brevo.api-key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    public void sendOTP(String toEmail, String otp, String type) {
        String subject = type.equals("LOGIN")
                ? "UEP SORS – Your Login Verification Code"
                : "UEP SORS – Verify Your Email";

        String action = type.equals("LOGIN") ? "log in" : "complete your registration";

        String html = """
                <div style="font-family:Arial,sans-serif;max-width:480px;margin:auto;border:1px solid #ddd;border-radius:12px;overflow:hidden;">
                  <div style="background:#131045;padding:24px;text-align:center;">
                    <h2 style="color:#FFD700;margin:0;">UEP SORS</h2>
                    <p style="color:#fff;margin:4px 0 0;">Student Organization Registration System</p>
                  </div>
                  <div style="padding:32px;background:#ffffff;">
                    <p style="font-size:16px;color:#333;">Use the code below to %s:</p>
                    <div style="text-align:center;margin:24px 0;">
                      <span style="display:inline-block;background:#FFD700;color:#131045;font-size:36px;font-weight:700;letter-spacing:10px;padding:16px 32px;border-radius:10px;">
                        %s
                      </span>
                    </div>
                    <p style="font-size:14px;color:#666;">This code expires in <strong>10 minutes</strong>.</p>
                    <p style="font-size:14px;color:#999;">If you did not request this, please ignore this email.</p>
                  </div>
                </div>
                """.formatted(action, otp);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        Map<String, Object> body = Map.of(
                "sender", Map.of("name", "UEP SORS", "email", senderEmail),
                "to", List.of(Map.of("email", toEmail)),
                "subject", subject,
                "htmlContent", html
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        restTemplate.postForObject(BREVO_API_URL, request, String.class);
    }
}