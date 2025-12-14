package com.mycompany.airlineticketingsystem.service;

import com.mycompany.airlineticketingsystem.model.Customer;
import com.mycompany.airlineticketingsystem.model.Staff;
import com.mycompany.airlineticketingsystem.repository.UserRepositoryImpl;
import java.util.Optional;

public class AuthenticationService {
    
    private UserRepositoryImpl userRepo = new UserRepositoryImpl();

    // STAFF LOGIN LOGIC
    public boolean loginStaff(String staffId, String inputPassword) {
        Optional<Staff> staff = userRepo.findStaffById(staffId);
        
        if (staff.isPresent()) {
            // Check if password matches
            if (staff.get().getPassword().equals(inputPassword)) {
                return true; // Success
            }
        }
        return false; // Failed
    }

    // CUSTOMER LOGIN LOGIC
    public boolean loginCustomer(String icNo, String inputPassword) {
        Optional<Customer> cust = userRepo.findCustomerByIc(icNo);
        
        if (cust.isPresent()) {
            if (cust.get().getPassword().equals(inputPassword)) {
                return true; // Success
            }
        }
        return false; // Failed
    }
}