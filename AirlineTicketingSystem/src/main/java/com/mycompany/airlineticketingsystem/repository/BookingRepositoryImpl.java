package com.mycompany.airlineticketingsystem.repository;

import com.mycompany.airlineticketingsystem.config.DatabaseConnection;
import com.mycompany.airlineticketingsystem.model.Booking; // Ensure this import is correct based on where you created Booking java
import java.sql.*;

public class BookingRepositoryImpl implements BookingRepository {

    public BookingRepositoryImpl() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS booking (" +
                "booking_id VARCHAR(50) PRIMARY KEY, " +
                "flight_id VARCHAR(50), " +
                "ic_no VARCHAR(50), " +
                "status VARCHAR(20), " +
                "booking_date TIMESTAMP, " +
                "seat_id VARCHAR(50), " +
                "total_price DECIMAL(10, 2))";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ Checked/Created 'booking' table.");
        } catch (SQLException e) {
            System.err.println("❌ Error creating table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void save(Booking booking) {
        String sql = "INSERT INTO booking (booking_id, flight_id, ic_no, status, booking_date, seat_id, total_price) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, booking.getBookingId());
            stmt.setString(2, booking.getFlightId());
            stmt.setString(3, booking.getIcNo());
            stmt.setString(4, booking.getStatus());
            stmt.setTimestamp(5, Timestamp.valueOf(booking.getBookingDate()));
            stmt.setString(6, booking.getSeatId());
            stmt.setBigDecimal(7, booking.getTotalPrice());

            stmt.executeUpdate();
            System.out.println("✅ Booking saved successfully: " + booking.getBookingId());

        } catch (SQLException e) {
            System.err.println("❌ Error saving booking: " + e.getMessage());
            e.printStackTrace();
            // Critical: Throw exception so the Service/Controller knows it failed!
            throw new RuntimeException("Database Insertion Failed: " + e.getMessage(), e);
        }
    }

    @Override
    public String generateNewBookingId() {
        String sql = "SELECT booking_id FROM booking ORDER BY booking_id DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String lastId = rs.getString("booking_id");
                // Expecting "B001", "B002", etc.
                if (lastId.startsWith("B")) {
                    int nextNum = Integer.parseInt(lastId.substring(1));
                    return String.format("B%03d", nextNum + 1);
                }
            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }

        return "B001"; // Default or Fallback
    }
    

    @Override
    public void updateSeatStatus(String flightId, String seatNumber) {
        String sql = "UPDATE seat SET status = ? WHERE flight_id = ? AND seat_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "BOOKED");
            stmt.setString(2, flightId);
            stmt.setString(3, seatNumber);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Seat status updated to BOOKED for " + seatNumber);
            } else {
                System.err.println("⚠️ Warning: Seat not found for update: " + seatNumber);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error updating seat status: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
