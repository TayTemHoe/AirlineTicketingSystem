package com.mycompany.airlineticketingsystem.model;

public class TicketEntity {
    private String ticketId;
    private String flightId;
    private String seatId;
    private String seatNumber;
    private String passengerPassportNumber; // Renamed from passengerName
    private String customerIcNumber; // Renamed from customerId

    public TicketEntity() {
    }

    public TicketEntity(String ticketId, String flightId, String seatId, String seatNumber,
            String passengerPassportNumber,
            String customerIcNumber) {
        this.ticketId = ticketId;
        this.flightId = flightId;
        this.seatId = seatId;
        this.seatNumber = seatNumber;
        this.passengerPassportNumber = passengerPassportNumber;
        this.customerIcNumber = customerIcNumber;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getPassengerPassportNumber() {
        return passengerPassportNumber;
    }

    public void setPassengerPassportNumber(String passengerPassportNumber) {
        this.passengerPassportNumber = passengerPassportNumber;
    }

    public String getCustomerIcNumber() {
        return customerIcNumber;
    }

    public void setCustomerIcNumber(String customerIcNumber) {
        this.customerIcNumber = customerIcNumber;
    }
}
