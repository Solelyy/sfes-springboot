package com.sfes.superadmin.account.service;

import com.sfes.common.exceptions.EmailSendingException;
import com.sfes.superadmin.account.email.EmailContext;
import com.sfes.superadmin.account.email.EmailTemplateRenderer;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SuccessfulActivationService {
    private final JavaMailSender javaMailSender;
    private final EmailTemplateRenderer emailTemplateRenderer;

    @Value("${app.email.from}")
    private String from;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public void sendEmail(EmailContext emailContext) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            String html = emailTemplateRenderer.render(emailContext);

            helper.setFrom(emailContext.from());
            helper.setTo(emailContext.to());
            helper.setSubject(emailContext.subject());
            helper.setText(html, true);

            javaMailSender.send(message);

        } catch (MessagingException e) {
            log.error("Failed to send email", e);
            throw new EmailSendingException("Failed to send email");
        }
    }

    public void sendSuccessActivation(String email, String firstName){
        EmailContext emailContext = new EmailContext(
                from,
                email,
                "Account Activated - QCU SFES",
                "email/successful-activation",
                Map.of(
                        "name", firstName,
                        "loginUrl", frontendUrl
                )
        );

        sendEmail(emailContext);
    }
}
