package com.mycompany.airlineticketingsystem.service;

import com.mycompany.airlineticketingsystem.model.Customer;
import com.mycompany.airlineticketingsystem.model.Staff;
import com.mycompany.airlineticketingsystem.repository.UserRepositoryImpl;
import com.password4j.Password; // 1. Import Password4j
import java.util.List;
import java.util.Optional;

public class AuthenticationService {
    
    private UserRepositoryImpl userRepo = new UserRepositoryImpl();

    // --- LOGIN LOGIC (VERIFY HASH) ---

    public Optional<Staff> loginStaff(String staffId, String inputPassword) {
        Optional<Staff> staff = userRepo.findStaffById(staffId);
        
        if (staff.isPresent()) {
            // 2. Verify Hash instead of .equals()
            // We verify the 'inputPassword' against the 'stored hash' from DB
            boolean verified = Password.check(inputPassword, staff.get().getPassword()).withArgon2();
            
            if (verified) {
                return staff;
            }
        }
        return Optional.empty();
    }

    public Optional<Customer> loginCustomer(String icNo, String inputPassword) {
        Optional<Customer> cust = userRepo.findCustomerByIc(icNo);
        
        if (cust.isPresent()) {
            // 2. Verify Hash
            boolean verified = Password.check(inputPassword, cust.get().getPassword()).withArgon2();
            
            if (verified) {
                return cust;
            }
        }
        return Optional.empty();
    }
    
    // --- REGISTRATION LOGIC (CREATE HASH) ---

    public boolean registerCustomer(Customer cust) {
        if (userRepo.findCustomerByIc(cust.getIcNo()).isPresent()) {
            System.out.println("User already exists!");
            return false; 
        }
        
        // 3. Hash the password before saving
        String hashedPassword = Password.hash(cust.getPassword()).withArgon2().getResult();
        
        // Create a new object with the hashed password (to avoid mutating the original passed object if needed)
        Customer secureCust = new Customer(
            cust.getIcNo(), cust.getName(), cust.getEmail(), 
            cust.getPhoneNumber(), cust.getGender(), 
            hashedPassword // <--- Save Hash
        );

        return userRepo.saveCustomer(secureCust);
    }

    public boolean registerNewStaff(String requesterId, Staff newStaff) {
        Optional<Staff> requester = userRepo.findStaffById(requesterId);
        
        if (requester.isPresent() && "Manager".equalsIgnoreCase(requester.get().getPosition())) {
            
            String nextId = userRepo.generateNextStaffId();
            
            // 3. Hash the password
            String hashedPassword = Password.hash(newStaff.getPassword()).withArgon2().getResult();

            Staff finalStaff = new Staff(
                nextId, newStaff.getName(), newStaff.getEmail(), 
                newStaff.getPhoneNumber(), newStaff.getGender(), 
                newStaff.getPosition(), 
                hashedPassword // <--- Save Hash
            );
            
            return userRepo.saveStaff(finalStaff);
        }
        
        System.out.println("❌ Authorization Failed: Only Managers can register new staff.");
        return false;
    }

    // --- UPDATE LOGIC (RE-HASH IF CHANGED) ---

    public boolean updateCustomerProfile(Customer c) {
        // NOTE: We assume the controller passes the PLAIN text new password here.
        // Ideally, check if the password field was actually changed before hashing.
        // For this implementation, we simply re-hash whatever is passed.
        
        String hashedPassword = Password.hash(c.getPassword()).withArgon2().getResult();
        
        Customer secureUpdate = new Customer(
            c.getIcNo(), c.getName(), c.getEmail(), 
            c.getPhoneNumber(), c.getGender(), 
            hashedPassword
        );
        
        return userRepo.updateCustomer(secureUpdate);
    }

    public boolean updateStaffProfile(Staff s) {
        String hashedPassword = Password.hash(s.getPassword()).withArgon2().getResult();
        
        Staff secureUpdate = new Staff(
            s.getStaffId(), s.getName(), s.getEmail(), 
            s.getPhoneNumber(), s.getGender(), 
            s.getPosition(), 
            hashedPassword
        );
        
        return userRepo.updateStaff(secureUpdate);
    }
    
    public List<Staff> getAllStaff() {
        return userRepo.findAllStaff();
    }
}