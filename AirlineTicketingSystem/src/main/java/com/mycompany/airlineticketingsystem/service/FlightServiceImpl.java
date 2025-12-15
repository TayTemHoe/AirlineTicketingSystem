package com.mycompany.airlineticketingsystem.service;

import com.mycompany.airlineticketingsystem.model.Flight;
import com.mycompany.airlineticketingsystem.model.Plane;
import com.mycompany.airlineticketingsystem.model.Seat;
import com.mycompany.airlineticketingsystem.enums.SeatStatus;
import com.mycompany.airlineticketingsystem.enums.SeatType;
import com.mycompany.airlineticketingsystem.repository.FlightRepository;
import com.mycompany.airlineticketingsystem.repository.FlightRepositoryImpl;
import com.mycompany.airlineticketingsystem.repository.SeatRepository;
import com.mycompany.airlineticketingsystem.repository.SeatRepositoryImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final SeatRepository seatRepository;

    // ✅ NEW: Local Cache Variable
    private static List<Flight> cachedFlights = null;

    public FlightServiceImpl() {
        this.flightRepository = new FlightRepositoryImpl();
        this.seatRepository = new SeatRepositoryImpl();
    }

    @Override
    public List<Flight> getAllFlights() {
        // ✅ If cache exists, return it directly (Instant Load)
        if (cachedFlights != null && !cachedFlights.isEmpty()) {
            return cachedFlights;
        }
        
        // Else, fetch from DB and store in cache
        System.out.println("Fetching flights from Database...");
        cachedFlights = flightRepository.findAll();
        return cachedFlights;
    }
    
    // Call this when you want to force reload (e.g., Refresh button)
    public void refreshCache() {
        System.out.println("Refreshing Cache...");
        cachedFlights = flightRepository.findAll();
    }

    @Override
    public void createFlight(Flight flight, Plane plane) {
        // 1. Save to DB
        flightRepository.save(flight);
        List<Seat> newSeats = generateSeatsForPlane(flight.getFlightId(), plane.getCapacity());
        seatRepository.saveAll(newSeats);
        
        // ✅ 2. Update Local Cache (No need to refetch everything)
        if (cachedFlights != null) {
            cachedFlights.add(flight);
        }
    }

    @Override
    public void deleteFlight(String flightId) {
        flightRepository.delete(flightId);
        
        // ✅ Update Local Cache
        if (cachedFlights != null) {
            cachedFlights.removeIf(f -> f.getFlightId().equals(flightId));
        }
    }
    
    @Override
    public void updateFlight(Flight flight) {
        flightRepository.save(flight);
        // Refresh cache to ensure data consistency
        refreshCache();
    }

    @Override
    public List<Flight> searchFlights(String from, String to, LocalDate date) {
        // ✅ Optimize: Search in the Local Cache instead of DB Query
        List<Flight> source = getAllFlights(); // Ensures cache is loaded
        
        return source.stream()
                .filter(f -> (from == null || from.isEmpty() || f.getDepartCountry().equals(from)))
                .filter(f -> (to == null || to.isEmpty() || f.getArriveCountry().equals(to)))
                .filter(f -> (date == null || f.getDepartTime().toLocalDate().equals(date)))
                .collect(Collectors.toList());
    }

    // ... (Keep getSeatsForFlight, generateSeatsForPlane, getAllPlanes, getFlightById as is) ...
    @Override
    public List<Seat> getSeatsForFlight(String flightId) {
        return seatRepository.findByFlightId(flightId);
    }
    
    @Override
    public List<Plane> getAllPlanes() {
        return flightRepository.findAllPlane();
    }
    
    @Override
    public Optional<Flight> getFlightById(String flightId) {
        return flightRepository.findById(flightId);
    }

    private List<Seat> generateSeatsForPlane(String flightId, int capacity) {
        List<Seat> generatedSeats = new ArrayList<>();
        int rows = capacity / 4; 
        char[] colLetters = {'A', 'B', 'C', 'D'};
        for (int row = 1; row <= rows; row++) {
            for (int col = 0; col < 4; col++) {
                SeatType type = (row == 1) ? SeatType.BUSINESS : SeatType.ECONOMY;
                String seatNum = row + String.valueOf(colLetters[col]);
                String uniqueId = flightId + "-" + seatNum;
                Seat seat = new Seat(uniqueId, seatNum, type, SeatStatus.AVAILABLE, flightId);
                generatedSeats.add(seat);
            }
        }
        return generatedSeats;
    }
}

