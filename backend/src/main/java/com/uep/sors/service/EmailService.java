package com.uep.sors.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOTP(String toEmail, String otp, String type) throws MessagingException {
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

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(html, true);

        mailSender.send(message);
    }
}