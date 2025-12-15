package com.mycompany.airlineticketingsystem.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Booking {
    private String bookingId;
    private String flightId;
    private String icNo;
    private String status;
    private LocalDateTime bookingDate;
    private String seatId;
    private BigDecimal totalPrice;

    public Booking() {
    }

    public Booking(String bookingId, String flightId, String icNo, String status, LocalDateTime bookingDate,
            String seatId, BigDecimal totalPrice) {
        this.bookingId = bookingId;
        this.flightId = flightId;
        this.icNo = icNo;
        this.status = status;
        this.bookingDate = bookingDate;
        this.seatId = seatId;
        this.totalPrice = totalPrice;
    }

    // Getters and Setters
    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getIcNo() {
        return icNo;
    }

    public void setIcNo(String icNo) {
        this.icNo = icNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
