package com.mycompany.airlineticketingsystem.service;

import com.mycompany.airlineticketingsystem.model.Ticket;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

public class TicketPdfService {

    public Path generate(Ticket ticket, Path outputDir) throws IOException {
        Files.createDirectories(outputDir);

        String fileName = String.format("Ticket_%s_%s.pdf", ticket.getFlightId(), ticket.getSeatNumber());
        Path out = outputDir.resolve(fileName);

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                float x = 60, y = 760;

                cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 18);
                cs.beginText();
                cs.newLineAtOffset(x, y);
                cs.showText("Airline Ticket");
                cs.endText();

                y -= 30;
                cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);

                y = line(cs, x, y, "From:", ticket.getFrom());
                y = line(cs, x, y, "To:", ticket.getTo());
                y = line(cs, x, y, "Flight:", ticket.getFlightId());
                y = line(cs, x, y, "Seat:", ticket.getSeatNumber());
                y = line(cs, x, y, "IC Number:", ticket.getCustomerIcNumber());
                y = line(cs, x, y, "Passport:", ticket.getPassportNumber());
                y = line(cs, x, y, "Departure:", ticket.getDepartureTime().format(fmt));
                y = line(cs, x, y, "Arrival:", ticket.getArrivalTime().format(fmt));

                // simple footer
                cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 10);
                cs.beginText();
                cs.newLineAtOffset(x, 80);
                cs.showText("Please arrive early. Keep this ticket for boarding.");
                cs.endText();
            }

            doc.save(out.toFile());
        }

        return out;
    }

    private float line(PDPageContentStream cs, float x, float y, String label, String value) throws IOException {
        cs.beginText();
        cs.newLineAtOffset(x, y);
        cs.showText(label);
        cs.endText();

        cs.beginText();
        cs.newLineAtOffset(x + 90, y);
        cs.showText(value == null ? "-" : value);
        cs.endText();

        return y - 18;
    }
}
