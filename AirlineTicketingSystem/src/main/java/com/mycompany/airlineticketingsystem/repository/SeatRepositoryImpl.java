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
import com.mycompany.airlineticketingsystem.enums.SeatStatus;
import com.mycompany.airlineticketingsystem.enums.SeatType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatRepositoryImpl implements SeatRepository {

    @Override
    public List<Seat> findByFlightId(String flightId) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM seat WHERE flight_id = ? ORDER BY seat_id";

        // ✅ FIX: Do NOT close connection here
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, flightId);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    seats.add(new Seat(
                        rs.getString("seat_id"),
                        rs.getString("seat_number"),
                        SeatType.valueOf(rs.getString("type")),
                        SeatStatus.valueOf(rs.getString("status")),
                        rs.getString("flight_id")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }

    // Update saveAll to also NOT close connection
    @Override
    public void saveAll(List<Seat> seats) {
        String sql = "INSERT INTO seat (seat_id, seat_number, type, status, flight_id) VALUES (?, ?, ?, ?, ?)";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (Seat seat : seats) {
                    stmt.setString(1, seat.getSeatId());
                    stmt.setString(2, seat.getSeatNumber());
                    stmt.setString(3, seat.getType().name());
                    stmt.setString(4, seat.getStatus().name());
                    stmt.setString(5, seat.getFlightId());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
}