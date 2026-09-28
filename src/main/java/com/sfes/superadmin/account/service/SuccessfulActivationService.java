package com.sfes.superadmin.account.service;

import com.sfes.common.classes.EmailContext;
import com.sfes.common.classes.email.EmailDelivery;
import com.sfes.common.classes.email.EmailDeliveryRepository;
import com.sfes.common.classes.email.EmailStatus;
import com.sfes.common.utility.email.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SuccessfulActivationService {
    private final EmailSender emailSender;
    private final EmailDeliveryRepository emailDeliveryRepository;

    @Value("${app.email.from}")
    private String from;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Async
    public void sendSuccessActivation(Long emailDeliveryId, String email, String firstName){
        EmailDelivery emailDelivery = emailDeliveryRepository.findById(emailDeliveryId)
                .orElseThrow();

        try {
            String loginUrl = frontendUrl + "/login";

            EmailContext emailContext = new EmailContext(
                    from,
                    email,
                    "Account Activated - QCU SFES",
                    "email/successful-activation",
                    Map.of(
                            "name", firstName,
                            "loginUrl", loginUrl
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
}
