package com.sfes.auth.service;

import com.sfes.common.classes.EmailContext;
import com.sfes.common.utility.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResetPasswordEmailService {
    private final EmailSender emailSender;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${app.email.from}")
    private String from;

    @Async
    public void sendResetPasswordEmail(
            String recipientEmail,
            String recipientName,
            String resetPasswordToken
    ) {
        String resetPasswordLink = buildResetPasswordLink(resetPasswordToken);

        EmailContext emailContext = new EmailContext(
                from,
                recipientEmail,
                "Reset Password - QCU SFES",
                "email/reset-password",
                Map.of(
                        "name", recipientName,
                        "resetPasswordLink", resetPasswordLink
                )
        );

        emailSender.sendEmail(emailContext);
    }

    private String buildResetPasswordLink(String resetPasswordToken) {
        return frontendUrl + "/reset-password?token=" + resetPasswordToken;
    }

    @Async
    public void sendSuccessfulResetPassword(String email, String firstName){
        EmailContext emailContext = new EmailContext(
                from,
                email,
                "Successful Password Reset - QCU SFES",
                "email/successful-reset-password",
                Map.of("name", firstName, "loginUrl", frontendUrl)
        );

        emailSender.sendEmail(emailContext);
    }
}

