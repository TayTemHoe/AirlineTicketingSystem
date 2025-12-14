package com.mycompany.airlineticketingsystem.model;

public abstract class Person {
    protected String name;
    protected String email;
    protected String phoneNumber;
    protected String gender;

    public Person(String name, String email, String phoneNumber, String gender) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber; 
        this.gender = gender;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getGender() { return gender; }
}