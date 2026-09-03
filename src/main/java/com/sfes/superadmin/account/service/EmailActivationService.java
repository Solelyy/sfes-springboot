package com.sfes.superadmin.account.service;

import com.sfes.common.classes.EmailContext;
import com.sfes.common.utility.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailActivationService {

    private final EmailSender emailSender;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${app.email.from}")
    private String from;

    @Async
    public void sendActivationEmail(
            String recipientEmail,
            String recipientName,
            String activationToken
    ) {
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
    }

    private String buildActivationLink(String activationToken) {
        return frontendUrl + "/activate?token=" + activationToken;
    }
}