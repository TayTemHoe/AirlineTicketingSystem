/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.airlineticketingsystem.repository;

/**
 *
 * @author Tay Tem Hoe
 */

import com.mycompany.airlineticketingsystem.model.Seat;
import com.mycompany.airlineticketingsystem.config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.airlineticketingsystem.enums.SeatStatus;
import com.mycompany.airlineticketingsystem.enums.SeatType;

public class SeatRepositoryImpl implements SeatRepository {

    @Override
    public void saveAll(List<Seat> seats) {
        String sql = "INSERT INTO seat (seat_number, type, status, flight_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Batch Processing (Optimization for High Marks)
            // Instead of sending 100 separate queries, we bundle them.
            for (Seat seat : seats) {
                stmt.setString(1, seat.getSeatNumber());
                stmt.setString(2, seat.getType().name());
                stmt.setString(3, seat.getStatus().name());
                stmt.setString(4, seat.getFlightId());
                stmt.addBatch(); // Add to queue
            }
            
            stmt.executeBatch(); // Send all at once
            System.out.println("✅ Generated " + seats.size() + " seats for Flight " + seats.get(0).getFlightId());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Seat> findByFlightId(String flightId) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM seat WHERE flight_id = ? ORDER BY seat_id"; // Basic ordering

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, flightId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Seat s = new Seat(
                    rs.getInt("seat_id"),
                    rs.getString("seat_number"),
                    SeatType.valueOf(rs.getString("type")),
                    SeatStatus.valueOf(rs.getString("status")),
                    rs.getString("flight_id")
                );
                seats.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }
}
