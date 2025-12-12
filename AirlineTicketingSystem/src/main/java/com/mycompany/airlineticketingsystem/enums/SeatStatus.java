package com.mycompany.airlineticketingsystem.enums;

/**
 * Enum representing the status of a seat.
 * 
 * @author Modernized Architecture
 */

public enum SeatStatus {
    AVAILABLE,  // Replaces "Empty"
    BOOKED,     // Replaces "Booked"
    BLOCKED;    // Replaces "Selected" (e.g., during checkout)

    public static SeatStatus fromString(String status) {
        try {
            return SeatStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            return AVAILABLE; // Default
        }
    }
}

