package com.mycompany.airlineticketingsystem.repository;

import com.mycompany.airlineticketingsystem.config.DatabaseConnection;
import com.mycompany.airlineticketingsystem.model.TicketEntity;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketEntityRepositoryImpl implements TicketEntityRepository {

    public TicketEntityRepositoryImpl() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        // Table: ticket. Columns: ticket_id, flight_id, seat_id, seat,
        // passenger_passport_number,
        // customer_ic_number
        // RE-SET SCHEMA
        String sqlDrop = "DROP TABLE IF EXISTS ticket";
        String sql = "CREATE TABLE IF NOT EXISTS ticket (" +
                "ticket_id VARCHAR(50) PRIMARY KEY, " +
                "flight_id VARCHAR(50), " +
                "seat_id VARCHAR(50), " +
                "seat VARCHAR(20), " +
                "passenger_passport_number VARCHAR(100), " +
                "customer_ic_number VARCHAR(50))";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement()) {
            // stmt.execute(sqlDrop); // Reset table - DISABLED
            stmt.execute(sql);
            System.out.println("✅ Checked/Created 'ticket' table.");
        } catch (SQLException e) {
            System.err.println("❌ Error creating 'ticket' table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void save(TicketEntity ticket) {
        String sql = "INSERT INTO ticket (ticket_id, flight_id, seat_id, seat, passenger_passport_number, customer_ic_number) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ticket.getTicketId());
            stmt.setString(2, ticket.getFlightId());
            stmt.setString(3, ticket.getSeatId());
            stmt.setString(4, ticket.getSeatNumber());
            stmt.setString(5, ticket.getPassengerPassportNumber());
            stmt.setString(6, ticket.getCustomerIcNumber());

            stmt.executeUpdate();
            System.out.println("✅ Ticket saved: " + ticket.getTicketId());

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save Ticket: " + e.getMessage(), e);
        }
    }

    @Override
    public String generateNewTicketId() {
        String sql = "SELECT ticket_id FROM ticket ORDER BY ticket_id DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String lastId = rs.getString("ticket_id");
                // Expected format "T001", "T002", etc.
                if (lastId != null && lastId.startsWith("T") && lastId.length() > 1) {
                    try {
                        int idNum = Integer.parseInt(lastId.substring(1));
                        return String.format("T%03d", idNum + 1);
                    } catch (NumberFormatException e) {
                        System.err.println(
                                "Warning: Could not parse previous ticket ID " + lastId + ". Resetting to T001.");
                    }
                }
            }
            return "T001"; // Default if no records or parse error
        } catch (SQLException e) {
            throw new RuntimeException("Error generating new ticket ID: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<TicketEntity> findByCustomerIc(String icNo) {
        List<TicketEntity> tickets = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE customer_ic_number = ? ORDER BY ticket_id DESC";

        // ✅ FIX: Connection outside try block
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, icNo);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    tickets.add(new TicketEntity(
                        rs.getString("ticket_id"),
                        rs.getString("flight_id"),
                        rs.getString("seat_id"),
                        rs.getString("seat"),
                        rs.getString("passenger_passport_number"),
                        rs.getString("customer_ic_number")
                    ));
                }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }
}
