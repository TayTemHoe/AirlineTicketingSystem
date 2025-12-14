package com.mycompany.airlineticketingsystem.service;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ReportService {

    private static final String DB_URL = "jdbc:postgresql://db.ajdaciskaffuvaanizlw.supabase.co:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASS = "ATS!y3s2G5MNT";

    // 1. FINANCIAL REPORT (Revenue)
    public Map<String, Double> getRevenueByMethod() {
        Map<String, Double> data = new HashMap<>();
        String sql = "SELECT payment_method, SUM(total_amount) as total FROM payments GROUP BY payment_method";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                data.put(rs.getString("payment_method"), rs.getDouble("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    // 2. OPERATIONAL REPORT (Top Destinations)
    // ✅ UPDATED: Now uses the REAL 'bookings' and 'flight' tables
    public Map<String, Integer> getTopDestinations() {
        Map<String, Integer> data = new HashMap<>();
        
        // We join BOOKINGS -> FLIGHT to see where people are actually going
        String sql = "SELECT f.arrive_country, COUNT(b.booking_id) as total_bookings " +
                     "FROM bookings b " +
                     "JOIN flight f ON b.flight_id = f.flight_id " +
                     "WHERE b.status = 'CONFIRMED' " +  // Only count confirmed bookings
                     "GROUP BY f.arrive_country " +
                     "ORDER BY total_bookings DESC LIMIT 5";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                data.put(rs.getString("arrive_country"), rs.getInt("total_bookings"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching Destinations: " + e.getMessage());
        }
        return data;
    }
   
    public Map<String, Integer> getSeatClassDistribution() {
        Map<String, Integer> data = new java.util.HashMap<>();
        String sql = "SELECT s.type, COUNT(b.booking_id) as total " +
                     "FROM bookings b JOIN seat s ON b.seat_id = s.seat_id " +
                     "WHERE b.status = 'CONFIRMED' GROUP BY s.type";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) data.put(rs.getString("type"), rs.getInt("total"));
        } catch (SQLException e) { e.printStackTrace(); }
        return data;
    }

    // ADD THIS for Tab 3
    public Map<String, Integer> getCustomerGenderDistribution() {
        Map<String, Integer> data = new java.util.HashMap<>();
        String sql = "SELECT c.gender, COUNT(b.booking_id) as total " +
                     "FROM bookings b JOIN customer c ON b.ic_no = c.ic_no " +
                     "GROUP BY c.gender";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) data.put(rs.getString("gender"), rs.getInt("total"));
        } catch (SQLException e) { e.printStackTrace(); }
        return data;
    }
}