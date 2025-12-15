package com.mycompany.airlineticketingsystem.service;

import com.mycompany.airlineticketingsystem.model.Booking;
import com.mycompany.airlineticketingsystem.repository.BookingRepositoryImpl;
import com.mycompany.airlineticketingsystem.service.BookingService;
import com.mycompany.airlineticketingsystem.service.BookingServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BookingServiceTest {

    private BookingService bookingService;

    @BeforeEach
    public void setUp() {
        bookingService = new BookingServiceImpl();
    }

    @Test
    public void testGenerateNewBookingId() {
        String bookingId = bookingService.generateNewBookingId();
        Assertions.assertNotNull(bookingId, "Booking ID should not be null");
        Assertions.assertTrue(bookingId.startsWith("B"), "Booking ID should start with 'B'");
    }

    @Test
    public void testSaveBooking() {
        // Create dummy booking
        Booking booking = new Booking();
        String newId = bookingService.generateNewBookingId();
        booking.setBookingId(newId + "_TEST"); // Ensure uniqueness for test
        booking.setFlightId("TEST_FLIGHT_ID");
        booking.setIcNo("TEST_IC");
        booking.setStatus("CONFIRMED");
        booking.setBookingDate(LocalDateTime.now());
        booking.setSeatId("1A");
        booking.setTotalPrice(new BigDecimal("100.00"));

        Assertions.assertDoesNotThrow(() -> bookingService.saveBooking(booking));
    }
}
