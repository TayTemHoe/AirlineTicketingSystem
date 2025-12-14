package com.mycompany.airlineticketingsystem.model;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Tay Tem Hoe
 */
import com.mycompany.airlineticketingsystem.enums.Gender;

public class Passenger extends Person {
    
    private String icPassport; // Specific to traveling

    public Passenger(int passengerId, String name, String icPassport, String email, String phone, Gender gender, String seatNumber, String flightId) {
        super(name, email, phone, gender); // Call Parent (Person)
        this.icPassport = icPassport;
    }
    
    // Overloaded Constructor (for creating new objects before saving to DB)
    public Passenger(String name, String icPassport, String email, String phone, Gender gender, String seatNumber, String flightId) {
        super(name, email, phone, gender);
        this.icPassport = icPassport;
    }

    // --- Getters & Setters ---
    public void setIcPassport(String icPassport) { 
        this.icPassport = icPassport;
    }
    public String getIcPassport() { return icPassport; }
    
}
