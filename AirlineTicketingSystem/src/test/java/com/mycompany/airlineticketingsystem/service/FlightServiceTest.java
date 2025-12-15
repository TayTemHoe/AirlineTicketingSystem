/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.airlineticketingsystem.service;

import com.mycompany.airlineticketingsystem.config.DatabaseConnection;
import com.mycompany.airlineticketingsystem.model.Flight;
import com.mycompany.airlineticketingsystem.model.Plane;
import com.mycompany.airlineticketingsystem.model.Seat;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FlightServiceTest {

    private static FlightService flightService;
    private static String testFlightId;
    
    // ✅ FIX: Use a Short ID (Max 10 chars)
    private static final String TEST_PLANE_ID = "TP99"; 

    @BeforeAll
    public static void setUp() {
        flightService = new FlightServiceImpl();
        
        // ✅ FIX: Generate a short Flight ID (e.g., "TF12345")
        testFlightId = "TF" + (System.currentTimeMillis() % 100000); 
        
        ensureTestPlaneExists();
    }

    private static void ensureTestPlaneExists() {
        String sql = "INSERT INTO plane (plane_id, model, capacity) VALUES (?, ?, ?) ON CONFLICT (plane_id) DO NOTHING";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, TEST_PLANE_ID);
            stmt.setString(2, "TestJet");
            stmt.setInt(3, 4); 
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Setup failed: " + e.getMessage());
        }
    }

    @Test
    @Order(1)
    public void testCreateFlightAndGenerateSeats() {
        System.out.println("Testing Create Flight: " + testFlightId);

        Plane plane = new Plane(TEST_PLANE_ID, "TestJet", 4);
        
        Flight flight = new Flight.Builder()
                .id(testFlightId)
                .route("TestOrigin", "TestDest")
                .timing(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(2))
                .prices(100.0, 200.0)
                .plane(plane.getPlaneId()) 
                .build();

        assertDoesNotThrow(() -> flightService.createFlight(flight, plane));

        List<Flight> allFlights = flightService.getAllFlights();
        boolean exists = allFlights.stream().anyMatch(f -> f.getFlightId().equals(testFlightId));
        assertTrue(exists, "Flight should be saved in the database");

        List<Seat> seats = flightService.getSeatsForFlight(testFlightId);
        assertNotNull(seats);
        assertEquals(4, seats.size(), "Should generate 4 seats");
    }

    @Test
    @Order(2)
    public void testSearchFlight() {
        List<Flight> results = flightService.searchFlights("TestOrigin", "TestDest", java.time.LocalDate.now().plusDays(1));
        assertFalse(results.isEmpty());
        assertEquals(testFlightId, results.get(0).getFlightId());
    }

    @Test
    @Order(3)
    public void testDeleteFlight() {
        flightService.deleteFlight(testFlightId);
        
        List<Flight> results = flightService.searchFlights("TestOrigin", "TestDest", null);
        boolean exists = results.stream().anyMatch(f -> f.getFlightId().equals(testFlightId));
        
        assertFalse(exists, "Flight should be deleted");
    }
    
    @AfterAll
    public static void tearDown() {
        // Clean up
        String sql = "DELETE FROM plane WHERE plane_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, TEST_PLANE_ID);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
