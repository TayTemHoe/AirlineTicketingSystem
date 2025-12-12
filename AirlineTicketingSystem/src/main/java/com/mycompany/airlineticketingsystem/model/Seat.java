package com.mycompany.airlineticketingsystem.model;

import com.mycompany.airlineticketingsystem.enums.SeatType;
import com.mycompany.airlineticketingsystem.enums.SeatStatus;

public class Seat {

    private int id;                 // Database ID (Primary Key)
    private String seatNumber;      // e.g., "1A", "10C"
    private SeatType type;          // Enum: ECONOMY/BUSINESS
    private SeatStatus status;      // Enum: AVAILABLE/BOOKED
    private String flightId;        // Foreign Key to Flight

    // Constructors
    public Seat() {
    }

    public Seat(int id, String seatNumber, SeatType type, SeatStatus status, String flightId) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.type = type;
        this.status = status;
        this.flightId = flightId;
    }

    // --- Helper Logic (Moved from Legacy) ---
    public boolean isAvailable() {
        return this.status == SeatStatus.AVAILABLE;
    }

    // --- Getters and Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public SeatType getType() { return type; }
    public void setType(SeatType type) { this.type = type; }

    public SeatStatus getStatus() { return status; }
    public void setStatus(SeatStatus status) { this.status = status; }

    public String getFlightId() { return flightId; }
    public void setFlightId(String flightId) { this.flightId = flightId; }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s (%s)", seatNumber, type, status, flightId);
    }
}
