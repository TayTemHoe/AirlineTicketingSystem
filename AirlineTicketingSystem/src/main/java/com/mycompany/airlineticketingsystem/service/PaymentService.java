package com.mycompany.airlineticketingsystem.service;

import com.mycompany.airlineticketingsystem.model.Payment;
import java.sql.*;

public class PaymentService {

    private static final String DB_URL = "jdbc:postgresql://db.ajdaciskaffuvaanizlw.supabase.co:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASS = "ATS!y3s2G5MNT"; 

    // 1. GET BILL (Check how much they owe)
    public double getPendingPaymentAmount(int bookingId) {
        double amount = 0.0;
        String sql = "SELECT total_price FROM bookings WHERE booking_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookingId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                amount = rs.getDouble("total_price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return amount;
    }

    // 2. PAY BILL
    public boolean processPayment(Payment payment) {
        String insertSql = "INSERT INTO payments (booking_id, total_amount, card_last_four, payment_method, status, payment_date) VALUES (?, ?, ?, ?, ?, ?)";
        // Update booking status to CONFIRMED
        String updateSql = "UPDATE bookings SET status = 'CONFIRMED' WHERE booking_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            conn.setAutoCommit(false); // Transaction Start

            // A. Insert Payment
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setInt(1, payment.getBookingID());
                pstmt.setDouble(2, payment.getAmount());
                pstmt.setString(3, payment.getCardNo()); 
                pstmt.setString(4, payment.getPaymentMethod());
                pstmt.setString(5, "COMPLETED");
                pstmt.setObject(6, payment.getPaymentDate());
                pstmt.executeUpdate();
            }

            // B. Update Booking Status
            try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                pstmt.setInt(1, payment.getBookingID());
                pstmt.executeUpdate();
            }

            conn.commit(); // Transaction End
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}