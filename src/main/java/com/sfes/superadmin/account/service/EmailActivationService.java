package com.sfes.superadmin.account.service;

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
public class EmailActivationService {
    private final EmailDeliveryRepository emailDeliveryRepository;
    private final EmailSender emailSender;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${app.email.from}")
    private String from;

    @Async
    public void sendActivationEmail(
            Long emailDeliveryId,
            String recipientEmail,
            String recipientName,
            String activationToken
    ) {
        EmailDelivery emailDelivery =
                emailDeliveryRepository.findById(emailDeliveryId)
                        .orElseThrow();

        try {
            String activationLink = buildActivationLink(activationToken);

            EmailContext emailContext = new EmailContext(
                    from,
                    recipientEmail,
                    "Activate your SFES account",
                    "email/account-activation",
                    Map.of(
                            "name", recipientName,
                            "activationLink", activationLink
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

    private String buildActivationLink(String activationToken) {
        return frontendUrl + "/activate?token=" + activationToken;
    }
}