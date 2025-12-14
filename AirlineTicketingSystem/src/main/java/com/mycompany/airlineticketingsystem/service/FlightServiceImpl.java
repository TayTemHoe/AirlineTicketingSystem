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

    private final FlightRepository flightRepository;
    private final SeatRepository seatRepository;

    public FlightServiceImpl() {
        this.flightRepository = new FlightRepositoryImpl();
        this.seatRepository = new SeatRepositoryImpl();
    }

    @Override
    public void createFlight(Flight flight, Plane plane) {
        // 1. Save Flight
        flightRepository.save(flight);

        // 2. Generate Seats with NEW ID FORMAT
        List<Seat> newSeats = generateSeatsForPlane(flight.getFlightId(), plane.getCapacity());

        // 3. Save Seats
        seatRepository.saveAll(newSeats);
    }

    // New Update Method
    public void updateFlight(Flight flight) {
        flightRepository.save(flight); // save() usually handles UPSERT (Update if exists)
    }

    // New Delete Method
    public void deleteFlight(String flightId) {
        flightRepository.delete(flightId);
    }

    @Override
    public List<Flight> searchFlights(String from, String to, LocalDate date) {
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

    private List<Seat> generateSeatsForPlane(String flightId, int capacity) {
        List<Seat> generatedSeats = new ArrayList<>();
        int rows = capacity / 4; 
        char[] colLetters = {'A', 'B', 'C', 'D'};

        for (int row = 1; row <= rows; row++) {
            for (int col = 0; col < 4; col++) {
                SeatType type = (row == 1) ? SeatType.BUSINESS : SeatType.ECONOMY;
                
                String seatNum = row + String.valueOf(colLetters[col]);
                
                // NEW ID LOGIC: "FlightID-SeatNum" (e.g. MH370-1A)
                String uniqueId = flightId + "-" + seatNum;

                Seat seat = new Seat(uniqueId, seatNum, type, SeatStatus.AVAILABLE, flightId);
                generatedSeats.add(seat);
            }
        }
        return generatedSeats;
    }
    
    public List<Plane> getAllPlanes() {
        return flightRepository.findAllPlane();
    }
}

