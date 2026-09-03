package com.sfes.common.utility.email.adapter;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.sfes.common.exceptions.EmailSendingException;
import com.sfes.common.classes.EmailContext;
import com.sfes.common.utility.email.EmailSender;
import com.sfes.common.utility.email.EmailTemplateRenderer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = "app.email.provider",
        havingValue = "resend"
)
@Slf4j
@RequiredArgsConstructor
public class ResendEmailAdapter implements EmailSender {

    private final Resend resend;
    private final EmailTemplateRenderer templateRenderer;

    @Override
    public void sendEmail(EmailContext emailContext) {

        try {
            String html = templateRenderer.render(emailContext);

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from(emailContext.from())
                    .to(emailContext.to())
                    .subject(emailContext.subject())
                    .html(html)
                    .build();

            resend.emails().send(params);

        } catch (Exception e) {
            log.error("Failed to send email via Resend", e);

            throw new EmailSendingException(
                    "Failed to send email"
            );
        }
    }
}