package com.mycompany.airlineticketingsystem.service;

import com.mycompany.airlineticketingsystem.config.DatabaseConnection;
import com.mycompany.airlineticketingsystem.enums.Gender;
import com.mycompany.airlineticketingsystem.model.Customer;
import com.mycompany.airlineticketingsystem.model.Staff;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationServiceTest {

    @Test
    public void testLoginSuccess() {
        AuthenticationService auth = new AuthenticationService();
        
        // 1. Test STAFF (Assumes S001/112233 exists in DB)
        Optional<Staff> result1 = auth.loginStaff("S001", "Tb@123");
        
        // 2. Test CUSTOMER (Assumes this user exists in DB)
        Optional<Customer> result2 = auth.loginCustomer("910101-10-1111", "Tb@123"); 
        
        assertTrue(result1.isPresent(), "Staff login should succeed");
        assertTrue(result2.isPresent(), "Customer login should succeed");
    }

    @Test
    public void testLoginFailure() {
        AuthenticationService auth = new AuthenticationService();
        
        // 1. Fail STAFF
        Optional<Staff> result1 = auth.loginStaff("WrongStaffID", "WrongPass");
        
        // 2. Fail CUSTOMER 
        Optional<Customer> result2 = auth.loginCustomer("WrongIC", "WrongPass");
        
        // ✅ FIXED: We expect the Optional to be EMPTY (because login failed)
        assertTrue(result1.isEmpty(), "Staff login should return empty (fail) with wrong password");
        assertTrue(result2.isEmpty(), "Customer login should return empty (fail) with wrong password");
    }
    
    @Test
    public void testCustomerRegistration() {
        AuthenticationService auth = new AuthenticationService();
        String testIc = "999999-99-9999";
        
        // Ensure clean slate before test
        deleteCustomer(testIc);

        Customer newCust = new Customer(
            testIc, 
            "Unit Test User", 
            "test@unit.com", 
            "0123456789", 
            Gender.MALE, 
            "password123"
        );

        // Attempt register
        boolean isRegistered = auth.registerCustomer(newCust);
        
        assertTrue(isRegistered, "First registration should succeed");
        
        // Verify login works
        var loggedIn = auth.loginCustomer(testIc, "password123");
        assertTrue(loggedIn.isPresent(), "Should be able to login after registering");
    }

    // ✅ NEW TEST: Register Duplicate Failure
    @Test
    public void testRegisterDuplicateFailure() {
        AuthenticationService auth = new AuthenticationService();
        String dupIc = "888888-88-8888";
        
        // Ensure clean slate
        deleteCustomer(dupIc);

        Customer user = new Customer(
            dupIc, "Dup User", "dup@test.com", "111", Gender.FEMALE, "pass"
        );

        // 1. First Registration -> Should Pass
        boolean firstAttempt = auth.registerCustomer(user);
        assertTrue(firstAttempt, "First registration should succeed");

        // 2. Second Registration (Same IC) -> Should Fail
        boolean secondAttempt = auth.registerCustomer(user);
        assertFalse(secondAttempt, "Second registration with same IC should fail");
        
        // Cleanup
        deleteCustomer(dupIc);
    }
    
    @AfterAll
    public static void tearDown() {
        // Cleanup main test user
        deleteCustomer("999999-99-9999");
    }

    // Helper method to keep tests clean
    private static void deleteCustomer(String ic) {
        String sql = "DELETE FROM customer WHERE ic_no = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ic);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}