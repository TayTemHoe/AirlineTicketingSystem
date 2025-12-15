package com.mycompany.airlineticketingsystem.repository;

import com.mycompany.airlineticketingsystem.model.Booking;

public interface BookingRepository {
    void save(Booking booking);

    String generateNewBookingId();

    void updateSeatStatus(String flightId, String seatNumber);
   
}
