package com.mycompany.airlineticketingsystem.model;

public class PassengerEntity {
    private String passportNumber;
    private String name;

    public PassengerEntity() {
    }

    public PassengerEntity(String passportNumber, String name) {
        this.passportNumber = passportNumber;
        this.name = name;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
