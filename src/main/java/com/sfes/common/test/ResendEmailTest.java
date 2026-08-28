package com.sfes.common.test;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

public class ResendEmailTest {
    static void main (String[] args) {
        sendEmailActivation();
    }

    public static void sendEmailActivation() {

        Resend resend = new Resend("re_deMV2UvH_HDebzWw93cASqzkG3kVTuTeW");

        String activationLink = "http://localhost:8080?token=SGVsbG8gV29ybGQh";

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("onboarding@resend.dev")
                .to("jessanazarenedietagozun@gmail.com")
                .subject("Activate your SFES account")
                .html("""
                        <h2>Welcome to SFES!</h2>

                        <p>
                            Your Student-Faculty Evaluation System account
                            has been created by the administrator.
                        </p>

                        <p>
                            Click the button below to activate your account:
                        </p>

                        <p>
                            <a href="%s">Activate Now</a>
                        </p>

                        <p>
                            This invitation link will expire after the
                            configured period.
                        </p>

                        <p>
                            If you did not expect this invitation, you can
                            safely ignore this email.
                        </p>

                        <p>
                            Regards,<br>
                            SFES Administration
                        </p>
                        """.formatted(activationLink))
                .build();

        try {
            CreateEmailResponse response = resend.emails().send(params);
            System.out.println("Email sent: " + response);
        } catch (Exception e) {
            System.out.println("Failed sending email activation: " + e);
        }
    }
}

