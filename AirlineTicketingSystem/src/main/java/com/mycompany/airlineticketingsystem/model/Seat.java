package com.mycompany.airlineticketingsystem.model;

import com.mycompany.airlineticketingsystem.enums.SeatType;
import com.mycompany.airlineticketingsystem.enums.SeatStatus;

public class Seat {

    private String id;              // CHANGED from int to String (e.g., "MH370-1A")
    private String seatNumber;      // e.g., "1A"
    private SeatType type;          
    private SeatStatus status;      
    private String flightId;        

    public Seat() {}

    public Seat(String id, String seatNumber, SeatType type, SeatStatus status, String flightId) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.type = type;
        this.status = status;
        this.flightId = flightId;
    }

    // Getters and Setters
    public String getSeatId() { return id; }
    public void setSeatId(String id) { this.id = id; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public SeatType getType() { return type; }
    public void setType(SeatType type) { this.type = type; }

    public SeatStatus getStatus() { return status; }
    public void setStatus(SeatStatus status) { this.status = status; }

    public String getFlightId() { return flightId; }
    public void setFlightId(String flightId) { this.flightId = flightId; }
}
