package com.mycompany.airlineticketingsystem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;

public class EmailServiceTest {

    @Test
    public void testSendEmailFailureWithInvalidCreds() throws Exception {
        EmailService emailService = new EmailService();
        Path tempFile = Files.createTempFile("test_ticket", ".pdf");

        // This should fail because credentials are fake
        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            emailService.sendTicket(
                    "smtp.example.com",
                    587,
                    "fakeuser@example.com",
                    "fakepassword",
                    "recipient@example.com",
                    "Test Subject",
                    "Test Body",
                    tempFile);
        });

        // Ensure we tried
        Assertions.assertNotNull(exception);

        Files.deleteIfExists(tempFile);
    }
}
