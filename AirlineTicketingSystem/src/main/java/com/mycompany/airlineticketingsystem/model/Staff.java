package com.mycompany.airlineticketingsystem.model;

public class Staff extends Person {
    private String staffId;
    private String password;
    private String position; 

    public Staff(String staffId, String name, String email, String phone, String gender, String position, String password) {
        super(name, email, phone, gender);
        this.staffId = staffId;
        this.position = position;
        this.password = password;
    }

    // Getters specific to Staff
    public String getStaffId() { return staffId; }
    public String getPosition() { return position; }
    public String getPassword() { return password; }
}