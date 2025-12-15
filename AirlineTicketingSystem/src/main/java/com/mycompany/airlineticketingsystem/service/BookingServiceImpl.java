package com.mycompany.airlineticketingsystem.service;

import com.mycompany.airlineticketingsystem.model.Booking;
import com.mycompany.airlineticketingsystem.repository.BookingRepository;
import com.mycompany.airlineticketingsystem.repository.BookingRepositoryImpl;

public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    public BookingServiceImpl() {
        this.bookingRepository = new BookingRepositoryImpl();
    }

    @Override
    public boolean saveBooking(Booking booking) {
        // We do NOT want to swallow the exception here.
        // The Controller needs to know WHY it failed.
        bookingRepository.save(booking);
        return true;
    }

    @Override
    public String generateNewBookingId() {
        return bookingRepository.generateNewBookingId();
    }

    @Override
    public void updateSeatStatus(String flightId, String seatNumber) {
        bookingRepository.updateSeatStatus(flightId, seatNumber);
    }
    
}
