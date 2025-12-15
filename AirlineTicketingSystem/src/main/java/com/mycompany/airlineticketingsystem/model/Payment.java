package com.mycompany.airlineticketingsystem.model;

import java.time.LocalDateTime;

public class Payment {
    private int paymentID;
    private String bookingID; // CHANGED from int to String to match Booking.java
    private double amount;
    private String cardNo; // store 4 digit
    private String paymentMethod;
    private LocalDateTime paymentDate;
    private String status;

    //Construstor
    public Payment(String bookingID, double amount, String cardNo, String paymentMethod) {
        this.bookingID = bookingID;
        this.amount = amount;
        this.cardNo = cardNo;
        this.paymentMethod = paymentMethod;
        this.paymentDate = LocalDateTime.now();
        this.status = "PENDING";
    }

    public int getPaymentID() { return paymentID; }
    public void setPaymentID(int id) { this.paymentID = id; }
    
    public String getBookingID() { return bookingID; } // CHANGED
    public double getAmount() { return amount; }
    public String getCardNo() { return cardNo; }
    public String getPaymentMethod() { return paymentMethod; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}