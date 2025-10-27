package org.prathame.malavi.common;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class MailService {
    @Inject
    Mailer mailer;

    private static final String APP_NAME = "Ecommerce";
    private static final String APP_URI = "http://localhost:4200/user";
    private static final String SUPPORT_EMAIL = "customer-support@ecommerce.org.in";


    public void sendWelcomeMail(String userEmail) {
        mailer.send(Mail.withText(userEmail, "Hello from Ecommm", "Test body from Quarkus using MailDev!"));
    }




    public void sendWelcomeMail(String userEmail, String userName, String resourcePath) {
        String subject = "Welcome to " + APP_NAME + "!";
        String htmlBody = loadTemplate(resourcePath , new HashMap<>())
                .replace("{userName}", userName)
                .replace("{appUri}", APP_URI)
                .replace("{mailFrom}", SUPPORT_EMAIL);

        mailer.send(Mail.withHtml(userEmail, subject, htmlBody)
        );
    }


    public void sendOrderPlacedMail(String userEmail, String userName, String orderId) {
        String subject = "Your Order #" + orderId + " has been placed";
        String htmlBody = loadTemplate("templates/order_placed.html",
                Map.of("userName", userName, "orderId", orderId, "mailFrom", SUPPORT_EMAIL)
        );
        mailer.send(Mail.withHtml(userEmail, subject, htmlBody));
    }

    public void sendOrderShippedMail(String userEmail, String userName, String orderId, String trackingUrl) {
        String subject = "Your Order #" + orderId + " has been shipped";
        String htmlBody = loadTemplate("templates/order_shipped.html",
                Map.of("userName", userName, "orderId", orderId, "trackingUrl", trackingUrl, "mailFrom", SUPPORT_EMAIL)
        );
        mailer.send(Mail.withHtml(userEmail, subject, htmlBody));
    }

    public void sendOrderDeliveredMail(String userEmail, String userName, String orderId) {
        String subject = "Your Order #" + orderId + " has been delivered";
        String htmlBody = loadTemplate("templates/order_delivered.html",
                Map.of("userName", userName, "orderId", orderId, "mailFrom", SUPPORT_EMAIL)
        );
        mailer.send(Mail.withHtml(userEmail, subject, htmlBody));
    }



    // Helper: load + variable replacement for template files
    private String loadTemplate(String resourcePath, Map<String, String> params) {
        try {
            String content = Files.readString(
                    Paths.get(getClass().getClassLoader().getResource(resourcePath).toURI()),
                    StandardCharsets.UTF_8
            );
            for (Map.Entry<String, String> entry : params.entrySet()) {
                content = content.replace("{" + entry.getKey() + "}", entry.getValue());
            }
            return content;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load email template", e);
        }
    }



    private String loadTemplate(String resourcePath) {
        try {
            return Files.readString(Paths.get(getClass().getClassLoader()
                    .getResource(resourcePath).toURI()), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load email template", e);
        }
    }
}
