/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.airlineticketingsystem.service;

import com.mycompany.airlineticketingsystem.config.DatabaseConnection;
import com.mycompany.airlineticketingsystem.enums.Gender;
import com.mycompany.airlineticketingsystem.model.Booking;
import com.mycompany.airlineticketingsystem.model.Flight;
import com.mycompany.airlineticketingsystem.model.Payment;
import com.mycompany.airlineticketingsystem.model.Plane;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail; // Import fail
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PaymentServiceTest {
    
    private PaymentService paymentService;
    private FlightService flightService;
    private BookingService bookingService;

    // Test Data IDs
    private String testBookingId;
    private String testFlightId;
    private String testIcNo;
    private String testSeatId;
    
    // ✅ FIX 1: Use Short ID (Under 10 chars)
    private static final String TEST_PLANE_ID = "TP99"; 

    @BeforeEach
    public void setUp() {
        paymentService = new PaymentService();
        flightService = new FlightServiceImpl();
        bookingService = new BookingServiceImpl();

        long time = System.currentTimeMillis() % 10000;
        
        // ✅ FIX 2: Shorten these IDs to satisfy VARCHAR(10)
        testFlightId = "TF" + time;   // e.g., TF4567 (6 chars)
        testIcNo = "IC" + time;       // e.g., IC4567 (6 chars)
        testBookingId = "BK" + time;  // e.g., BK4567 (6 chars)
        
        // Setup Database Dependencies (Stop test if any fail)
        createTestPlane();
        createTestFlight(); 
        testSeatId = testFlightId + "-1A"; 
        createTestCustomer();
        createTestBooking();
    }

    @Test
    public void testProcessPayment() {
        System.out.println("Testing Process Payment...");

        Payment payment = new Payment(testBookingId, 100.00,"1234567812345678","Credit Card");
        
        boolean success = paymentService.processPayment(payment);
        
        assertTrue(success, "Payment processing should return true");
    }

    @Test
    public void testGetPendingPaymentAmount() {
        double amount = paymentService.getPendingPaymentAmount(testBookingId);
        Assertions.assertEquals(100.0, amount, 0.01, "Should return the booking price of 100.00");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Cleaning up Payment Test Data...");
        deletePaymentForBooking(testBookingId);
        deleteBooking(testBookingId);
        flightService.deleteFlight(testFlightId);
        deleteCustomer(testIcNo);
        deletePlane(TEST_PLANE_ID);
    }

    // --- HELPER METHODS WITH ERROR CHECKING ---

    private void createTestPlane() {
        String sql = "INSERT INTO plane (plane_id, model, capacity) VALUES (?, ?, ?) ON CONFLICT (plane_id) DO NOTHING";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, TEST_PLANE_ID);
            stmt.setString(2, "TestJet");
            stmt.setInt(3, 4);
            stmt.executeUpdate();
        } catch (Exception e) { 
            // ✅ FIX 3: Fail immediately if this setup step errors
            fail("Setup Failed: Could not create Plane. " + e.getMessage());
        }
    }

    private void createTestFlight() {
        Plane plane = new Plane(TEST_PLANE_ID, "TestJet", 4);
        Flight flight = new Flight.Builder()
                .id(testFlightId)
                .route("Origin", "Dest")
                .timing(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(2))
                .prices(100.0, 200.0)
                .plane(plane.getPlaneId())
                .build();
        
        try {
            flightService.createFlight(flight, plane);
            // Verify it actually saved?
            if(flightService.getFlightById(testFlightId).isEmpty()) {
                fail("Setup Failed: Flight was not saved to DB (Check ID length?)");
            }
        } catch (Exception e) {
            fail("Setup Failed: Could not create Flight. " + e.getMessage());
        }
    }

    private void createTestCustomer() {
        String sql = "INSERT INTO customer (ic_no, name, email, phone_number, gender, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, testIcNo);
            stmt.setString(2, "Pay Tester");
            stmt.setString(3, "pay@test.com");
            stmt.setString(4, "0123");
            stmt.setString(5, Gender.MALE.name());
            stmt.setString(6, "pass");
            stmt.executeUpdate();
        } catch (Exception e) { 
            fail("Setup Failed: Could not create Customer. " + e.getMessage());
        }
    }

    private void createTestBooking() {
        Booking b = new Booking();
        b.setBookingId(testBookingId);
        b.setFlightId(testFlightId);
        b.setIcNo(testIcNo);
        b.setSeatId(testSeatId);
        b.setStatus("PENDING"); 
        b.setBookingDate(LocalDateTime.now());
        b.setTotalPrice(new BigDecimal("100.00"));
        
        // This relies on Flight and Customer existing.
        // If they failed above, this throws the Foreign Key error you saw.
        boolean saved = bookingService.saveBooking(b);
        if(!saved) {
             fail("Setup Failed: Could not create Booking (Check Foreign Keys)");
        }
    }

    // --- CLEANUP ---
    private void deletePaymentForBooking(String bookingId) {
        if(bookingId == null) return;
        String sql = "DELETE FROM payments WHERE booking_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bookingId);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private void deleteBooking(String bookingId) {
        if(bookingId == null) return;
        String sql = "DELETE FROM booking WHERE booking_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bookingId);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void deleteCustomer(String ic) {
        if(ic == null) return;
        String sql = "DELETE FROM customer WHERE ic_no = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ic);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
    
    private void deletePlane(String id) {
        String sql = "DELETE FROM plane WHERE plane_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
