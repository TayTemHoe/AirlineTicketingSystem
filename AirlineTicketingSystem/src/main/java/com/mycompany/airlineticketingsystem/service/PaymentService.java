package com.mycompany.airlineticketingsystem.service;

import com.mycompany.airlineticketingsystem.config.DatabaseConnection;
import com.mycompany.airlineticketingsystem.model.Payment;
import java.sql.*;

public class PaymentService {

    // 1. GET BILL (Check how much they owe)
    public double getPendingPaymentAmount(String bookingId) {
        double amount = 0.0;
        String sql = "SELECT total_price FROM booking WHERE booking_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, bookingId);
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
        
        // UPDATED: Table name 'booking' (singular) based on BookingRepositoryImpl
        String updateSql = "UPDATE booking SET status = 'CONFIRMED' WHERE booking_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // Transaction Start

            // A. Insert Payment
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setString(1, payment.getBookingID()); // String ID
                pstmt.setDouble(2, payment.getAmount());
                pstmt.setString(3, payment.getCardNo()); 
                pstmt.setString(4, payment.getPaymentMethod());
                pstmt.setString(5, "COMPLETED");
                pstmt.setTimestamp(6, Timestamp.valueOf(payment.getPaymentDate()));
                pstmt.executeUpdate();
            }

            // B. Update Booking Status
            try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                pstmt.setString(1, payment.getBookingID()); // String ID
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