package com.mycompany.airlineticketingsystem.service;

import com.github.benmanes.caffeine.cache.Cache; // 1. Import Caffeine
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mycompany.airlineticketingsystem.model.Flight;
import com.mycompany.airlineticketingsystem.model.Plane;
import com.mycompany.airlineticketingsystem.model.Seat;
import com.mycompany.airlineticketingsystem.repository.FlightRepository;
import com.mycompany.airlineticketingsystem.repository.FlightRepositoryImpl;
import com.mycompany.airlineticketingsystem.repository.SeatRepository;
import com.mycompany.airlineticketingsystem.repository.SeatRepositoryImpl;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final SeatRepository seatRepository;

    // 2. NEW: Caffeine Cache Definition
    // Key = String (e.g., "ALL_FLIGHTS"), Value = List of Flights
    private static final Cache<String, List<Flight>> flightCache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES) // 3. Expire data after 10 mins
            .maximumSize(100)                       // Limit to 100 keys
            .build();

    public FlightServiceImpl() {
        this.flightRepository = new FlightRepositoryImpl();
        this.seatRepository = new SeatRepositoryImpl();
    }

    @Override
    public List<Flight> getAllFlights() {
        // 4. SMART FETCH: Check Cache first
        // If "ALL_FLIGHTS" is in cache, return it.
        // If NOT, run the lambda function (repo.findAll), store it, and return it.
        return flightCache.get("ALL_FLIGHTS", k -> {
            System.out.println("⚡ Cache Miss: Fetching flights from Supabase...");
            return flightRepository.findAll();
        });
    }
    
    // Call this to manually force a refresh (e.g. Refresh Button)
    public void refreshCache() {
        System.out.println("🔄 Invalidating Cache...");
        flightCache.invalidate("ALL_FLIGHTS");
        getAllFlights(); // Re-fetch immediately
    }

    @Override
    public void createFlight(Flight flight, Plane plane) {
        flightRepository.save(flight);
        List<Seat> newSeats = generateSeatsForPlane(flight.getFlightId(), plane.getCapacity());
        seatRepository.saveAll(newSeats);
        
        // 5. CACHE INVALIDATION
        // Data changed, so clear the old cache
        flightCache.invalidate("ALL_FLIGHTS");
    }

    @Override
    public void deleteFlight(String flightId) {
        flightRepository.delete(flightId);
        
        // 5. CACHE INVALIDATION
        flightCache.invalidate("ALL_FLIGHTS");
    }
    
    @Override
    public void updateFlight(Flight flight) {
        flightRepository.save(flight);
        
        // 5. CACHE INVALIDATION
        flightCache.invalidate("ALL_FLIGHTS");
    }

    @Override
    public List<Flight> searchFlights(String from, String to, LocalDate date) {
        // 6. Search uses the Cached List (Super Fast)
        List<Flight> source = getAllFlights(); 
        
        return source.stream()
                .filter(f -> (from == null || from.isEmpty() || f.getDepartCountry().equals(from)))
                .filter(f -> (to == null || to.isEmpty() || f.getArriveCountry().equals(to)))
                .filter(f -> (date == null || f.getDepartTime().toLocalDate().equals(date)))
                .collect(Collectors.toList());
    }

    // ... (Keep getSeatsForFlight, generateSeatsForPlane, getAllPlanes, getFlightById as is) ...
    // Note: You could also cache 'Planes' or 'Seats' if you wanted, using different keys.
    
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
        // ... (Keep your existing logic) ...
        List<Seat> generatedSeats = new ArrayList<>();
        int rows = (int) Math.ceil((double) capacity / 4.0); 
        char[] colLetters = {'A', 'B', 'C', 'D'};
        int seatsGenerated = 0;
        for (int row = 1; row <= rows; row++) {
            for (int col = 0; col < 4; col++) {
                if (seatsGenerated >= capacity) break;
                com.mycompany.airlineticketingsystem.enums.SeatType type = (row == 1) ? com.mycompany.airlineticketingsystem.enums.SeatType.BUSINESS : com.mycompany.airlineticketingsystem.enums.SeatType.ECONOMY;
                String seatNum = row + String.valueOf(colLetters[col]);
                String uniqueId = flightId + "-" + seatNum;
                Seat seat = new Seat(uniqueId, seatNum, type, com.mycompany.airlineticketingsystem.enums.SeatStatus.AVAILABLE, flightId);
                generatedSeats.add(seat);
                seatsGenerated++;
            }
        }
        return generatedSeats;
    }
}

