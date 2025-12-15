package com.mycompany.airlineticketingsystem.service;

import com.mycompany.airlineticketingsystem.config.DatabaseConnection;
import com.mycompany.airlineticketingsystem.model.PassengerEntity;
import com.mycompany.airlineticketingsystem.service.PassengerService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PassengerServiceTest {

    private PassengerService passengerService;

    @BeforeEach
    public void setUp() {
        passengerService = new PassengerService();
    }

    @Test
    public void testSavePassenger() {
        PassengerEntity passenger = new PassengerEntity("TEST_PASSPORT_001", "Test User");
        Assertions.assertDoesNotThrow(() -> passengerService.savePassenger(passenger));
    }
    
        @AfterAll
    public static void tearDown() {
        // Clean up
        String sql = "DELETE FROM passenger WHERE passport_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "TEST_PASSPORT_001");
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
