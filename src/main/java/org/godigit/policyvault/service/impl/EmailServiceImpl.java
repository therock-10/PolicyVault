package org.godigit.policyvault.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.godigit.policyvault.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${policyvault.notification.to}")  // compliance officer email from property
    private String toEmail;

    @Value("${spring.mail.username}")  // sender email
    private String fromEmail;

    @Override
    public void sendEmail(String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();

        // Debug logs
        System.out.println("📧 DEBUG From Email: [" + fromEmail + "]");
        System.out.println("📧 DEBUG To Email: [" + toEmail + "]");

        // Trim just in case property has hidden whitespace
        String trimmedToEmail = (toEmail != null) ? toEmail.trim() : null;

        if (trimmedToEmail == null || trimmedToEmail.isEmpty()) {
            throw new IllegalArgumentException("❌ To email address is missing or invalid in application.properties");
        }

        message.setFrom(fromEmail.trim());
        message.setTo(trimmedToEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);

        System.out.println("✅ Email sent successfully to: " + trimmedToEmail);
    }
}
