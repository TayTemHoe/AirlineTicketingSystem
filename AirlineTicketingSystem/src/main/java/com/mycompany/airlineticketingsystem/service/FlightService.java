package com.mycompany.airlineticketingsystem.service;

import com.mycompany.airlineticketingsystem.model.Flight;
import com.mycompany.airlineticketingsystem.model.Plane;
import com.mycompany.airlineticketingsystem.model.Seat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FlightService {
    
    // Core Business Logic: Creates Flight AND generates Seats
    void createFlight(Flight flight, Plane plane);
    
    // Standard Search
    List<Flight> searchFlights(String from, String to, LocalDate date);
    
    // Seat Management
    List<Seat> getSeatsForFlight(String flightId);
    
    // Admin features
    List<Flight> getAllFlights();
    
    List<Plane> getAllPlanes();
    
    
    void updateFlight(Flight flight);
    void deleteFlight(String flightId);
    
   // ...  methods ...
   Optional<Flight> getFlightById(String flightId);
}

