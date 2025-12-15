package com.mycompany.airlineticketingsystem.model;

public class PassengerDetails {
    private final String fullName;
    private final String passportNumber;

    public PassengerDetails(String fullName, String passportNumber) {
        this.fullName = fullName;
        this.passportNumber = passportNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPassportNumber() {
        return passportNumber;
    }
}
