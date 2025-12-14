/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.airlineticketingsystem.session;

/**
 *
 * @author Tay Tem Hoe
 */
import com.mycompany.airlineticketingsystem.model.Staff;
import com.mycompany.airlineticketingsystem.model.Customer;

public class UserSession {
    private static UserSession instance;
    private Staff loggedInStaff;
    private Customer loggedInCustomer;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setStaff(Staff staff) {
        this.loggedInStaff = staff;
        this.loggedInCustomer = null; // Clear customer session
    }

    public void setCustomer(Customer customer) {
        this.loggedInCustomer = customer;
        this.loggedInStaff = null; // Clear staff session
    }

    public Staff getLoggedInStaff() { return loggedInStaff; }
    
    public Customer getLoggedInCustomer() { return loggedInCustomer; }
    
    public void cleanUserSession() {
        loggedInStaff = null;
        loggedInCustomer = null;
    }
}
