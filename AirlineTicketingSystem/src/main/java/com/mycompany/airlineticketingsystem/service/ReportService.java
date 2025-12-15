package com.mycompany.airlineticketingsystem.service;

import com.mycompany.airlineticketingsystem.config.DatabaseConnection;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ReportService {

    // 1. FINANCIAL REPORT (Revenue)
    public Map<String, Double> getRevenueByMethod() {
        Map<String, Double> data = new HashMap<>();
        String sql = "SELECT payment_method, SUM(total_amount) as total FROM payments GROUP BY payment_method";

        // ✅ FIX
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    data.put(rs.getString("payment_method"), rs.getDouble("total"));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return data;
    }

    // 2. OPERATIONAL REPORT (Top Destinations)
    // ✅ UPDATED: 'bookings' -> 'booking'
    public Map<String, Integer> getTopDestinations() {
        Map<String, Integer> data = new HashMap<>();
        String sql = "SELECT f.arrive_country, COUNT(b.booking_id) as total_bookings " +
                     "FROM booking b JOIN flight f ON b.flight_id = f.flight_id " +
                     "WHERE b.status = 'CONFIRMED' GROUP BY f.arrive_country " +
                     "ORDER BY total_bookings DESC LIMIT 5";

        // ✅ FIX
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    data.put(rs.getString("arrive_country"), rs.getInt("total_bookings"));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return data;
    }
   
    // 3. SEAT CLASS DISTRIBUTION
    // ✅ UPDATED: 'bookings' -> 'booking'
    public Map<String, Integer> getSeatClassDistribution() {
        Map<String, Integer> data = new HashMap<>();
        String sql = "SELECT s.type, COUNT(b.booking_id) as total " +
                     "FROM booking b JOIN seat s ON b.seat_id = s.seat_id " +
                     "WHERE b.status = 'CONFIRMED' GROUP BY s.type";
                     
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) data.put(rs.getString("type"), rs.getInt("total"));
        } catch (SQLException e) { e.printStackTrace(); }
        return data;
    }

    // 4. CUSTOMER DEMOGRAPHICS
    // ✅ CRITICAL FIX: Join Booking -> Ticket -> Customer to get Gender
    // Because booking.ic_no is now Passport Number (Passenger), not Customer IC.
    public Map<String, Integer> getCustomerGenderDistribution() {
        Map<String, Integer> data = new HashMap<>();
        String sql = "SELECT c.gender, COUNT(b.booking_id) as total " +
                     "FROM booking b " +
                     "JOIN ticket t ON b.seat_id = t.seat_id " + 
                     "JOIN customer c ON t.customer_ic_number = c.ic_no " +
                     "WHERE b.status = 'CONFIRMED' GROUP BY c.gender";
                     
        // ✅ FIX
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    data.put(rs.getString("gender"), rs.getInt("total"));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return data;
    }
}