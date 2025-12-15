package com.mycompany.airlineticketingsystem.repository;

import com.mycompany.airlineticketingsystem.model.Flight;
import com.mycompany.airlineticketingsystem.config.DatabaseConnection;
import com.mycompany.airlineticketingsystem.model.Plane;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlightRepositoryImpl implements FlightRepository {

    @Override
    public List<Flight> findAll() {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flight ORDER BY depart_time";

        // ✅ FIX: Get connection OUTSIDE the try-with-resources block
        try {
            Connection conn = DatabaseConnection.getConnection(); 
            // Only 'stmt' and 'rs' are auto-closed. 'conn' stays open.
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    flights.add(mapResultSetToFlight(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }

    // Apply the same fix to other methods (findById, searchFlights, etc.)
    @Override
    public Optional<Flight> findById(String flightId) {
        String sql = "SELECT * FROM flight WHERE flight_id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, flightId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) return Optional.of(mapResultSetToFlight(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return Optional.empty();
    }

    @Override
    public void save(Flight flight) {
        // ... (Keep SQL string) ...
        String sql = "INSERT INTO flight (flight_id, depart_country, arrive_country, depart_time, arrive_time, price_economy, price_business, plane_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                     "ON CONFLICT (flight_id) DO UPDATE SET " +
                     "depart_country = EXCLUDED.depart_country, " + // ... rest of your SQL
                     "arrive_country = EXCLUDED.arrive_country, " +
                     "depart_time = EXCLUDED.depart_time, " +
                     "arrive_time = EXCLUDED.arrive_time, " +
                     "price_economy = EXCLUDED.price_economy, " +
                     "price_business = EXCLUDED.price_business, " +
                     "plane_id = EXCLUDED.plane_id";

        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, flight.getFlightId());
                stmt.setString(2, flight.getDepartCountry());
                stmt.setString(3, flight.getArriveCountry());
                stmt.setTimestamp(4, Timestamp.valueOf(flight.getDepartTime()));
                stmt.setTimestamp(5, Timestamp.valueOf(flight.getArriveTime()));
                stmt.setBigDecimal(6, flight.getPriceEconomy());
                stmt.setBigDecimal(7, flight.getPriceBusiness());
                stmt.setString(8, flight.getPlaneId());
                stmt.executeUpdate();
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void delete(String flightId) {
        String sql = "DELETE FROM flight WHERE flight_id = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, flightId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // ... Keep mapResultSetToFlight and other methods ...
    private Flight mapResultSetToFlight(ResultSet rs) throws SQLException {
        return new Flight.Builder()
                .id(rs.getString("flight_id"))
                .route(rs.getString("depart_country"), rs.getString("arrive_country"))
                .timing(rs.getTimestamp("depart_time").toLocalDateTime(),
                        rs.getTimestamp("arrive_time").toLocalDateTime())
                .prices(rs.getBigDecimal("price_economy").doubleValue(),
                        rs.getBigDecimal("price_business").doubleValue())
                .plane(rs.getString("plane_id"))
                .build();
    }

    @Override
    public List<Flight> searchFlights(String from, String to, LocalDate date) {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flight WHERE depart_country = ? AND arrive_country = ? AND DATE(depart_time) = ?";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, from);
                stmt.setString(2, to);
                stmt.setDate(3, java.sql.Date.valueOf(date));
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) flights.add(mapResultSetToFlight(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return flights;
    }

    @Override
    public List<Plane> findAllPlane() {
        List<Plane> planes = new ArrayList<>();
        String sql = "SELECT * FROM plane";
        try {
            Connection conn = DatabaseConnection.getConnection();
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) planes.add(new Plane(rs.getString("plane_id"), rs.getString("model"), rs.getInt("capacity")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return planes;
    }
}
