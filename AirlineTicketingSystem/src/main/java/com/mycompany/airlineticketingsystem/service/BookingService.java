package com.mycompany.airlineticketingsystem.service;

import com.mycompany.airlineticketingsystem.model.Booking;

public interface BookingService {
    boolean saveBooking(Booking booking);

    String generateNewBookingId();

    void updateSeatStatus(String flightId, String seatNumber);
}
