package com.mycompany.airlineticketingsystem.enums;

/**
 * Enum representing the type of seat on a flight.
 * 
 * @author Modernized Architecture
 */
public enum SeatType {
    ECONOMY,
    BUSINESS;

    // Optional: Helper to convert DB string to Enum safely
    public static SeatType fromString(String type) {
        try {
            return SeatType.valueOf(type.toUpperCase());
        } catch (Exception e) {
            return ECONOMY; // Default fallback
        }
    }
}

