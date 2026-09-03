package com.sfes.common.utility.email;

import com.resend.Resend;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
        name = "app.email.provider",
        havingValue = "resend"
)
public class ResendApiConfig {

    @Bean
    public Resend resend(
            @Value("${api.resend-api-key}") String apiKey
    ) {
        return new Resend(apiKey);
    }
}