package com.mycompany.airlineticketingsystem.model;

import com.mycompany.airlineticketingsystem.enums.Gender;

public class Customer extends Person {
    private String icNo;     
    private String password; 

    public Customer(String icNo, String name, String email, String phone, Gender gender, String password) {
        super(name, email, phone, gender);
        this.icNo = icNo;
        this.password = password;
    }

    public String getIcNo() { return icNo; }
    public String getPassword() { return password; }
}