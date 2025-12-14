package com.mycompany.airlineticketingsystem.model;

import com.mycompany.airlineticketingsystem.enums.Gender;

public abstract class Person {
    protected String name;
    protected String email;
    protected String phoneNumber;
    protected Gender gender; // Changed from String to Enum

    public Person(String name, String email, String phoneNumber, Gender gender) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber; 
        this.gender = gender;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public Gender getGender() { return gender; }
}