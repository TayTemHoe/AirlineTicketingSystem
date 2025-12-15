package com.mycompany.airlineticketingsystem.repository;

import com.mycompany.airlineticketingsystem.config.DatabaseConnection;
import com.mycompany.airlineticketingsystem.model.PassengerEntity;
import java.sql.*;
import java.util.Optional;

public class PassengerRepositoryImpl implements PassengerRepository {

    public PassengerRepositoryImpl() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        // Table: passenger. Columns: passport_number, name
        // RE-SET SCHEMA: Drop old table if exists to fix column mismatch
        String sqlDrop = "DROP TABLE IF EXISTS passenger";
        String sqlCreate = "CREATE TABLE IF NOT EXISTS passenger (" +
                "passport_number VARCHAR(50) PRIMARY KEY, " +
                "name VARCHAR(100))";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement()) {
            // stmt.execute(sqlDrop); // disable dropping
            stmt.execute(sqlCreate);
            System.out.println("✅ Checked/Created 'passenger' table.");
        } catch (SQLException e) {
            System.err.println("❌ Error creating 'passenger' table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void save(PassengerEntity passenger) {
        // UPSERT logic: If passport_number exists, update name, otherwise insert.
        // Assuming Postgres:
        String sql = "INSERT INTO passenger (passport_number, name) VALUES (?, ?) " +
                "ON CONFLICT (passport_number) DO UPDATE SET name = EXCLUDED.name";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, passenger.getPassportNumber());
            stmt.setString(2, passenger.getName());

            stmt.executeUpdate();
            System.out.println("✅ Passenger saved/updated: " + passenger.getPassportNumber());

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save Passenger: " + e.getMessage(), e);
        }
    }
}
