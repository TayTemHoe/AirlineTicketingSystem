package com.mycompany.airlineticketingsystem.repository;

import com.mycompany.airlineticketingsystem.model.Flight;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FlightRepository {
    
    // Save a new flight or update an existing one
    void save(Flight flight);
    
    // Find a flight by its ID (e.g., "F001")
    Optional<Flight> findById(String flightId);
    
    // Get a list of all flights
    List<Flight> findAll();
    
    // Search for flights based on route and date
    List<Flight> searchFlights(String from, String to, LocalDate date);
    
    // Delete a flight
    void delete(String flightId);
}

