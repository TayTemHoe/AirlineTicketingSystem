package com.mycompany.airlineticketingsystem.model;

import java.time.LocalDateTime;

public class Ticket {
    private final String from;
    private final String to;
    private final String flightId;
    private final String seatNumber;
    private final String customerIcNumber; // Replaces passengerName
    private final String passportNumber;
    private final LocalDateTime departureTime;
    private final LocalDateTime arrivalTime;

    public Ticket(String from, String to, String flightId, String seatNumber,
            String customerIcNumber, String passportNumber,
            LocalDateTime departureTime, LocalDateTime arrivalTime) {
        this.from = from;
        this.to = to;
        this.flightId = flightId;
        this.seatNumber = seatNumber;
        this.customerIcNumber = customerIcNumber;
        this.passportNumber = passportNumber;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getFlightId() {
        return flightId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getCustomerIcNumber() {
        return customerIcNumber;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }
}
