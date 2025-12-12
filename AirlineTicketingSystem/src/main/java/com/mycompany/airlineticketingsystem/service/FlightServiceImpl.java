package com.mycompany.airlineticketingsystem.service;

import com.mycompany.airlineticketingsystem.enums.SeatStatus;
import com.mycompany.airlineticketingsystem.enums.SeatType;
import com.mycompany.airlineticketingsystem.model.Flight;
import com.mycompany.airlineticketingsystem.model.Plane;
import com.mycompany.airlineticketingsystem.model.Seat;
import com.mycompany.airlineticketingsystem.repository.FlightRepository;
import com.mycompany.airlineticketingsystem.repository.SeatRepository;
import com.mycompany.airlineticketingsystem.repository.FlightRepositoryImpl;
import com.mycompany.airlineticketingsystem.repository.SeatRepositoryImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FlightServiceImpl implements FlightService {

    // Dependencies (We manually inject them for now, Spring Boot does this auto in real frameworks)
    private final FlightRepository flightRepository;
    private final SeatRepository seatRepository;

    public FlightServiceImpl() {
        this.flightRepository = new FlightRepositoryImpl();
        this.seatRepository = new SeatRepositoryImpl();
    }

    @Override
    public void createFlight(Flight flight, Plane plane) {
        // 1. Validation (Business Rule)
        if (flight.getArriveTime().isBefore(flight.getDepartTime())) {
            throw new IllegalArgumentException("Arrival time cannot be before Departure time!");
        }

        // 2. Save the Flight Header
        flightRepository.save(flight);

        // 3. Generate Seats (Factory Logic)
        List<Seat> newSeats = generateSeatsForPlane(flight.getFlightId(), plane.getCapacity());

        // 4. Save Seats
        seatRepository.saveAll(newSeats);
    }

    @Override
    public List<Flight> searchFlights(String from, String to, LocalDate date) {
        // Simple delegator to repository
        return flightRepository.searchFlights(from, to, date);
    }

    @Override
    public List<Seat> getSeatsForFlight(String flightId) {
        return seatRepository.findByFlightId(flightId);
    }

    @Override
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    /**
     * FACTORY METHOD: Generates seats based on capacity.
     * Legacy Logic: 4 seats per row. Row 1 is Business, rest are Economy.
     */
    private List<Seat> generateSeatsForPlane(String flightId, int capacity) {
        List<Seat> generatedSeats = new ArrayList<>();
        int rows = capacity / 4; 
        
        char[] colLetters = {'A', 'B', 'C', 'D'};

        for (int row = 1; row <= rows; row++) {
            for (int col = 0; col < 4; col++) {
                
                // Determine Type: Row 1 is Business (High-Value rule)
                SeatType type = (row == 1) ? SeatType.BUSINESS : SeatType.ECONOMY;
                
                // Seat Number: "1A", "1B", etc.
                String seatNum = row + String.valueOf(colLetters[col]);

                Seat seat = new Seat();
                seat.setSeatNumber(seatNum);
                seat.setType(type);
                seat.setStatus(SeatStatus.AVAILABLE); // Default status
                seat.setFlightId(flightId);

                generatedSeats.add(seat);
            }
        }
        return generatedSeats;
    }
}

