package com.sfes.common.utility.email.adapter;

import com.sfes.common.exceptions.EmailSendingException;
import com.sfes.common.classes.EmailContext;
import com.sfes.common.utility.email.EmailSender;
import com.sfes.common.utility.email.EmailTemplateRenderer;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;

@Component
@ConditionalOnProperty(
        name = "app.email.provider",
        havingValue = "smtp"
)
@Slf4j
@RequiredArgsConstructor
public class SmtpEmailAdapter implements EmailSender {

    private final JavaMailSender mailSender;
    private final EmailTemplateRenderer emailTemplateRenderer;

    @Override
    public void sendEmail(EmailContext emailContext) {

        try {
            MimeMessage message = mailSender.createMimeMessage();

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

            mailSender.send(message);

        } catch (MessagingException e) {
            log.error("Failed to send email", e);
            throw new EmailSendingException("Failed to send email");
        }
    }
}