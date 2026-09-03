package com.sfes.superadmin.account.service;

import com.sfes.common.classes.EmailContext;
import com.sfes.common.utility.email.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SuccessfulActivationService {
    private final EmailSender emailSender;

    @Value("${app.email.from}")
    private String from;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Async
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

        emailSender.sendEmail(emailContext);
    }
}
