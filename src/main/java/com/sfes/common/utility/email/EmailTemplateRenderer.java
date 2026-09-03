package com.sfes.common.utility.email;

import com.sfes.common.classes.EmailContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class EmailTemplateRenderer {
    private final TemplateEngine templateEngine;

    public String render(EmailContext emailContext) {
        Context context = new Context();
        context.setVariables(emailContext.variables());

        return templateEngine.process(
                emailContext.template(),
                context
        );
    }
}
