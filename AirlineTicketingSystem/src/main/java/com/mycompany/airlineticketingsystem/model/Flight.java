package com.mycompany.airlineticketingsystem.model;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Flight {

    private String flightId;        // e.g., "F001"
    private String departCountry;
    private String arriveCountry;
    private LocalDateTime departTime;
    private LocalDateTime arriveTime;
    private BigDecimal priceEconomy;
    private BigDecimal priceBusiness;
    private String planeId;         // Foreign Key to Plane
    
    // Inventory: The list of seats for this flight
    private List<Seat> seats = new ArrayList<>();

    // --- Constructor (Private, use Builder instead) ---
    private Flight(String flightId, String departCountry, String arriveCountry, 
                   LocalDateTime departTime, LocalDateTime arriveTime, 
                   BigDecimal priceEconomy, BigDecimal priceBusiness, String planeId) {
        this.flightId = flightId;
        this.departCountry = departCountry;
        this.arriveCountry = arriveCountry;
        this.departTime = departTime;
        this.arriveTime = arriveTime;
        this.priceEconomy = priceEconomy;
        this.priceBusiness = priceBusiness;
        this.planeId = planeId;
    }
    
    // Default Constructor for Frameworks
    public Flight() {}

    // --- Business Logic Methods ---
    
    public long getDurationInHours() {
        if (departTime != null && arriveTime != null) {
            return Duration.between(departTime, arriveTime).toHours();
        }
        return 0;
    }
    
    public String getFormattedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return departTime.format(formatter) + " -> " + arriveTime.format(formatter);
    }

    // --- Getters and Setters ---
    public String getFlightId() { return flightId; }
    public void setFlightId(String flightId) { this.flightId = flightId; }

    public String getDepartCountry() { return departCountry; }
    public void setDepartCountry(String departCountry) { this.departCountry = departCountry; }

    public String getArriveCountry() { return arriveCountry; }
    public void setArriveCountry(String arriveCountry) { this.arriveCountry = arriveCountry; }

    public LocalDateTime getDepartTime() { return departTime; }
    public void setDepartTime(LocalDateTime departTime) { this.departTime = departTime; }

    public LocalDateTime getArriveTime() { return arriveTime; }
    public void setArriveTime(LocalDateTime arriveTime) { this.arriveTime = arriveTime; }

    public BigDecimal getPriceEconomy() { return priceEconomy; }
    public void setPriceEconomy(BigDecimal priceEconomy) { this.priceEconomy = priceEconomy; }

    public BigDecimal getPriceBusiness() { return priceBusiness; }
    public void setPriceBusiness(BigDecimal priceBusiness) { this.priceBusiness = priceBusiness; }

    public String getPlaneId() { return planeId; }
    public void setPlaneId(String planeId) { this.planeId = planeId; }

    public List<Seat> getSeats() { return seats; }
    public void setSeats(List<Seat> seats) { this.seats = seats; }

    @Override
    public String toString() {
        return flightId + ": " + departCountry + " -> " + arriveCountry + " (" + getFormattedTime() + ")";
    }

    // --- BUILDER PATTERN (For Design Patterns Requirement) ---
    public static class Builder {
        private String flightId;
        private String departCountry;
        private String arriveCountry;
        private LocalDateTime departTime;
        private LocalDateTime arriveTime;
        private BigDecimal priceEconomy;
        private BigDecimal priceBusiness;
        private String planeId;

        public Builder id(String id) { this.flightId = id; return this; }
        public Builder route(String from, String to) { 
            this.departCountry = from; 
            this.arriveCountry = to; 
            return this; 
        }
        public Builder timing(LocalDateTime dep, LocalDateTime arr) {
            this.departTime = dep;
            this.arriveTime = arr;
            return this;
        }
        public Builder prices(double eco, double bus) {
            this.priceEconomy = BigDecimal.valueOf(eco);
            this.priceBusiness = BigDecimal.valueOf(bus);
            return this;
        }
        public Builder plane(String planeId) { this.planeId = planeId; return this; }

        public Flight build() {
            return new Flight(flightId, departCountry, arriveCountry, departTime, arriveTime, priceEconomy, priceBusiness, planeId);
        }
    }
}
