package com.mycompany.airlineticketingsystem.service;

import com.mycompany.airlineticketingsystem.model.Customer;
import com.mycompany.airlineticketingsystem.model.Staff;
import com.mycompany.airlineticketingsystem.repository.UserRepositoryImpl;
import com.mycompany.airlineticketingsystem.session.UserSession;
import java.util.Optional;

public class AuthenticationService {
    
    private UserRepositoryImpl userRepo = new UserRepositoryImpl();

    // STAFF LOGIN LOGIC
    public Optional<Staff> loginStaff(String staffId, String inputPassword) {
        Optional<Staff> staff = userRepo.findStaffById(staffId);
        
        if (staff.isPresent() && staff.get().getPassword().equals(inputPassword)) {
            return staff; // Return the Staff object on success
        }
        return Optional.empty(); // Return empty on failure
    }

    //customer login
    public Optional<Customer> loginCustomer(String icNo, String inputPassword) {
        Optional<Customer> cust = userRepo.findCustomerByIc(icNo);
        
        if (cust.isPresent() && cust.get().getPassword().equals(inputPassword)) {
            return cust; // Return the Customer object on success
        }
        return Optional.empty();
    }
    
    public boolean registerCustomer(Customer cust) {
        // Check if IC already exists
        if (userRepo.findCustomerByIc(cust.getIcNo()).isPresent()) {
            System.out.println("User already exists!");
            return false; 
        }
        return userRepo.saveCustomer(cust);
    }

    // STAFF REGISTRATION (Restricted)
    // We require the 'requesterId' (the logged-in Manager) to authorize this.
    public boolean registerNewStaff(String requesterId, Staff newStaff) {
        Optional<Staff> requester = userRepo.findStaffById(requesterId);
        
        // 1. Authorization Check
        if (requester.isPresent() && "Manager".equalsIgnoreCase(requester.get().getPosition())) {
            
            // 2. Auto-Generate ID (Ignore whatever ID was passed)
            String nextId = userRepo.generateNextStaffId();
            
            // 3. Create new object with correct ID (Immutable pattern preferred, but setter works here)
            Staff finalStaff = new Staff(
                nextId, newStaff.getName(), newStaff.getEmail(), 
                newStaff.getPhoneNumber(), newStaff.getGender(), 
                newStaff.getPosition(), newStaff.getPassword()
            );
            
            return userRepo.saveStaff(finalStaff);
        }
        
        System.out.println("❌ Authorization Failed: Only Managers can register new staff.");
        return false;
    }

    // Update Profile Logic
    public boolean updateCustomerProfile(Customer customer) {
        return userRepo.updateCustomer(customer);
    }

    public boolean updateStaffProfile(Staff staff) {
        return userRepo.updateStaff(staff);
    }
}