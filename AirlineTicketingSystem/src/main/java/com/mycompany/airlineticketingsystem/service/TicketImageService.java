package com.mycompany.airlineticketingsystem.service;

import com.mycompany.airlineticketingsystem.model.Ticket;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

public class TicketImageService {

    public Path generate(Ticket ticket, Path outputDir) throws IOException {
        Files.createDirectories(outputDir);
        String fileName = String.format("Ticket_%s_%s.png", ticket.getFlightId(), ticket.getSeatNumber());
        Path outFile = outputDir.resolve(fileName);

        // Ticket Dimensions
        int width = 800;
        int height = 400;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Enable Anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // Header Strip
        g2d.setColor(new Color(0, 102, 204)); // Blue
        g2d.fillRect(0, 0, width, 80);

        // Header Text
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 36));
        g2d.drawString("Boarding Pass", 30, 55);

        g2d.setFont(new Font("SansSerif", Font.PLAIN, 18));
        g2d.drawString("Airline Ticketing System", 550, 50);

        // Main Content Info
        int startX = 40;
        int startY = 120;
        int lineHeight = 40;
        int col2X = 400;

        g2d.setColor(Color.BLACK);

        // Fonts
        Font labelFont = new Font("SansSerif", Font.PLAIN, 16);
        Font valueFont = new Font("SansSerif", Font.BOLD, 22);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");

        // Row 1: IC Number & Passport
        drawField(g2d, "IC NUMBER", ticket.getCustomerIcNumber(), startX, startY, labelFont, valueFont);
        drawField(g2d, "PASSPORT", ticket.getPassportNumber(), col2X, startY, labelFont, valueFont);

        // Row 2: From -> To
        drawField(g2d, "FROM", ticket.getFrom(), startX, startY + lineHeight * 2, labelFont, valueFont);
        drawField(g2d, "TO", ticket.getTo(), col2X, startY + lineHeight * 2, labelFont, valueFont);

        // Row 3: Flight & Seat
        drawField(g2d, "FLIGHT", ticket.getFlightId(), startX, startY + lineHeight * 4, labelFont, valueFont);
        drawField(g2d, "SEAT", ticket.getSeatNumber(), col2X, startY + lineHeight * 4, labelFont, valueFont);

        // Row 4: Dates
        drawField(g2d, "DEPARTURE", ticket.getDepartureTime().format(fmt), startX, startY + lineHeight * 5 + 15,
                labelFont, new Font("SansSerif", Font.BOLD, 18));

        // Borders/Lines for aesthetics
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(0, 80, width, 80); // Under header
        g2d.drawLine(0, 350, width, 350); // Above footer

        // Footer
        g2d.setColor(Color.DARK_GRAY);
        g2d.setFont(new Font("SansSerif", Font.ITALIC, 14));
        g2d.drawString("Please arrive at the boarding gate 30 minutes before departure.", 40, 380);

        g2d.dispose();

        // Save
        ImageIO.write(image, "png", outFile.toFile());

        return outFile;
    }

    private void drawField(Graphics2D g2d, String label, String value, int x, int y, Font labelFont, Font valueFont) {
        g2d.setColor(Color.GRAY);
        g2d.setFont(labelFont);
        g2d.drawString(label, x, y);

        g2d.setColor(Color.BLACK);
        g2d.setFont(valueFont);
        g2d.drawString(value == null ? "-" : value, x, y + 25);
    }
}
