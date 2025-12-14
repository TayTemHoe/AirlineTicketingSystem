package com.mycompany.airlineticketingsystem.model;

import java.time.LocalDateTime;

public class Payment {
    // These names match what your teammates expect (Legacy Support)
    private int paymentID;
    private int bookingID; // Added this to link with Wey Xian's code
    private double amount;
    private String cardNo; // Only stores last 4 digits now!
    private String paymentMethod;
    private LocalDateTime paymentDate;
    private String status;

    // Constructor
    public Payment(int bookingID, double amount, String cardNo, String paymentMethod) {
        this.bookingID = bookingID;
        this.amount = amount;
        this.cardNo = cardNo;
        this.paymentMethod = paymentMethod;
        this.paymentDate = LocalDateTime.now();
        this.status = "PENDING"; // Default status
    }

    // Getters (Standard)
    public int getPaymentID() { return paymentID; }
    public void setPaymentID(int id) { this.paymentID = id; }
    
    public int getBookingID() { return bookingID; }
    public double getAmount() { return amount; }
    public String getCardNo() { return cardNo; }
    public String getPaymentMethod() { return paymentMethod; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}