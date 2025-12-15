package com.mycompany.airlineticketingsystem.service;

import com.mycompany.airlineticketingsystem.model.Ticket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class TicketPdfServiceTest {

    @Test
    public void testGeneratePdf() throws IOException {
        TicketPdfService pdfService = new TicketPdfService();
        Path tempDir = Files.createTempDirectory("pdf_test_output");

        Ticket ticket = new Ticket(
                "Malaysia",
                "Japan",
                "MH370",
                "1A",
                "IC_123456",
                "PASS_ABC999",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(5));

        Path pdfPath = pdfService.generate(ticket, tempDir);

        Assertions.assertNotNull(pdfPath);
        Assertions.assertTrue(Files.exists(pdfPath), "PDF file should exist");
        Assertions.assertTrue(pdfPath.toString().endsWith(".pdf"), "Should have .pdf extension");
        Assertions.assertTrue(Files.size(pdfPath) > 0, "PDF should not be empty");

        // Cleanup
        Files.deleteIfExists(pdfPath);
        Files.deleteIfExists(tempDir);
    }
}
