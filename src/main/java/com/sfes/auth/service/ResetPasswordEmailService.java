package com.sfes.auth.service;

import com.sfes.common.classes.EmailContext;
import com.sfes.common.classes.email.EmailDelivery;
import com.sfes.common.classes.email.EmailDeliveryRepository;
import com.sfes.common.classes.email.EmailStatus;
import com.sfes.common.utility.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResetPasswordEmailService {
    private final EmailSender emailSender;
    private final EmailDeliveryRepository emailDeliveryRepository;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${app.email.from}")
    private String from;

    @Async
    public void sendResetPasswordEmail(
            Long emailDeliveryId,
            String recipientEmail,
            String recipientName,
            String resetPasswordToken
    ) {
        EmailDelivery emailDelivery = emailDeliveryRepository.findById(emailDeliveryId)
                .orElseThrow();

        try {
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

            emailDelivery.setStatus(EmailStatus.SENT);
            emailDelivery.setSentAt(Instant.now());

        } catch (Exception e) {
            emailDelivery.setStatus(EmailStatus.FAILED);
            emailDelivery.setFailedAt(Instant.now());
            emailDelivery.setLastError(e.getMessage());
        } finally {
            emailDeliveryRepository.save(emailDelivery);
        }
    }

    private String buildResetPasswordLink(String resetPasswordToken) {
        return frontendUrl + "/forgot-password/reset?token=" + resetPasswordToken;
    }

    @Async
    public void sendSuccessfulResetPassword(Long emailDeliveryId, String email, String firstName){
        EmailDelivery emailDelivery = emailDeliveryRepository.findById(emailDeliveryId)
                .orElseThrow();

        try {
            EmailContext emailContext = new EmailContext(
                    from,
                    email,
                    "Successful Password Reset - QCU SFES",
                    "email/successful-reset-password",
                    Map.of("name", firstName, "loginUrl", frontendUrl)
            );

            emailSender.sendEmail(emailContext);

            emailDelivery.setStatus(EmailStatus.SENT);
            emailDelivery.setSentAt(Instant.now());

        } catch (Exception e) {
            emailDelivery.setStatus(EmailStatus.FAILED);
            emailDelivery.setFailedAt(Instant.now());
            emailDelivery.setLastError(e.getMessage());
        } finally {
            emailDeliveryRepository.save(emailDelivery);
        }
    }
}

