package com.mycompany.airlineticketingsystem.service;

import com.mycompany.airlineticketingsystem.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationServiceTest {

    @Test
    public void testLoginSuccess() {
        // 1.setup
        AuthenticationService auth = new AuthenticationService();
        
        // 2. Test STAFF
        boolean result1 = auth.loginStaff("S001", "112233");
        
        // 3. Test CUSTOMER
        boolean result2 = auth.loginCustomer("910101-10-1111", "112233"); 
        
        assertTrue(result1, "Staff login should succeed");
        assertTrue(result2, "Customer login should succeed");
    }

    @Test
    public void testLoginFailure() {
        AuthenticationService auth = new AuthenticationService();
        
        // 1. Fail STAFF
        boolean result1 = auth.loginStaff("WrongStaffID", "WrongPass");
        
        // 2. Fail CUSTOMER 
        boolean result2 = auth.loginCustomer("WrongIC", "WrongPass");
        
        assertFalse(result1, "Staff login should fail with wrong password");
        assertFalse(result2, "Customer login should fail with wrong password");
    }
}