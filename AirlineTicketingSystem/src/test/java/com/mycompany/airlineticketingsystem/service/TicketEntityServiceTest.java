package com.mycompany.airlineticketingsystem.service;

import com.mycompany.airlineticketingsystem.config.DatabaseConnection;
import com.mycompany.airlineticketingsystem.enums.Gender;
import com.mycompany.airlineticketingsystem.model.Flight;
import com.mycompany.airlineticketingsystem.model.Plane;
import com.mycompany.airlineticketingsystem.model.TicketEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class TicketEntityServiceTest {

    private TicketEntityService ticketService;
    private FlightService flightService;

    // Test Data Identifiers
    private String testTicketId;
    private String testFlightId;
    private String testPlaneId;
    private String testSeatId;
    private String testCustomerIc;
    private String testPassengerPassport;

    @BeforeEach
    public void setUp() {
        ticketService = new TicketEntityService();
        flightService = new FlightServiceImpl();

        // 1. Generate Unique IDs to avoid collision
        long time = System.currentTimeMillis();
        testFlightId = "TF" + (time % 100000);
        testPlaneId = "TP" + (time % 1000); 
        testCustomerIc = "IC" + time;
        testPassengerPassport = "PP" + time;
        
        System.out.println("Setting up Test Data | Flight: " + testFlightId + " | Ticket Test");

        // 2. Ensure Dependencies Exist in DB
        createTestPlane();
        createTestFlight(); // Generates seats (including "1A")
        testSeatId = testFlightId + "-1A"; // Derived from flight logic
        
        createTestCustomer();
        createTestPassenger();
    }

    @Test
    public void testGenerateNewTicketId() {
        String ticketId = ticketService.generateNewTicketId();
        Assertions.assertNotNull(ticketId);
        Assertions.assertTrue(ticketId.startsWith("T"));
    }

    @Test
    public void testSaveTicket() {
        // 1. Prepare Ticket Object with Valid FKs
        testTicketId = ticketService.generateNewTicketId() + "_TEST";
        
        TicketEntity ticket = new TicketEntity(
                testTicketId,
                testFlightId,           // FK: Flight
                testSeatId,             // FK: Seat
                "1A",                   // Seat Number
                testPassengerPassport,  // FK: Passenger
                testCustomerIc          // FK: Customer
        );

        // 2. Execute & Verify
        assertDoesNotThrow(() -> ticketService.saveTicket(ticket),
                "Saving ticket should succeed with valid Foreign Keys");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Cleaning up Ticket Test Data...");
        
        // Delete in specific order to satisfy constraints
        deleteTicket(testTicketId);
        deletePassenger(testPassengerPassport);
        deleteCustomer(testCustomerIc);
        
        // Deleting flight usually handles seats via service or cascade
        flightService.deleteFlight(testFlightId);
        
        deletePlane(testPlaneId);
    }

    // --- HELPER METHODS (Direct SQL for setup) ---

    private void createTestPlane() {
        String sql = "INSERT INTO plane (plane_id, model, capacity) VALUES (?, ?, ?) ON CONFLICT (plane_id) DO NOTHING";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, testPlaneId);
            stmt.setString(2, "TestJet");
            stmt.setInt(3, 4);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void createTestFlight() {
        Plane plane = new Plane(testPlaneId, "TestJet", 4);
        Flight flight = new Flight.Builder()
                .id(testFlightId)
                .route("TestOrigin", "TestDest")
                .timing(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(2))
                .prices(100.0, 200.0)
                .plane(plane.getPlaneId())
                .build();

        try {
            flightService.createFlight(flight, plane);
        } catch (Exception e) {
            Assertions.fail("Failed to create test flight: " + e.getMessage());
        }
    }

    private void createTestCustomer() {
        String sql = "INSERT INTO customer (ic_no, name, email, phone_number, gender, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, testCustomerIc);
            stmt.setString(2, "Test User");
            stmt.setString(3, "test@test.com");
            stmt.setString(4, "0123456789");
            stmt.setString(5, Gender.MALE.name());
            stmt.setString(6, "password");
            stmt.executeUpdate();
        } catch (Exception e) {
            Assertions.fail("Failed to create test customer: " + e.getMessage());
        }
    }

    private void createTestPassenger() {
        String sql = "INSERT INTO passenger (passport_number, name) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, testPassengerPassport);
            stmt.setString(2, "Test Passenger");
            stmt.executeUpdate();
        } catch (Exception e) {
            Assertions.fail("Failed to create test passenger: " + e.getMessage());
        }
    }

    private void deleteTicket(String ticketId) {
        if (ticketId == null) return;
        String sql = "DELETE FROM ticket WHERE ticket_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ticketId);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void deleteCustomer(String icNo) {
        if (icNo == null) return;
        String sql = "DELETE FROM customer WHERE ic_no = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, icNo);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void deletePassenger(String passport) {
        if (passport == null) return;
        String sql = "DELETE FROM passenger WHERE passport_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, passport);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void deletePlane(String planeId) {
        if (planeId == null) return;
        String sql = "DELETE FROM plane WHERE plane_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, planeId);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
