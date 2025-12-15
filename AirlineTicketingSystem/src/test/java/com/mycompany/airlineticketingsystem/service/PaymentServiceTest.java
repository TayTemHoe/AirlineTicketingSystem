/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.airlineticketingsystem.service;

import com.mycompany.airlineticketingsystem.model.Payment;
import com.mycompany.airlineticketingsystem.service.PaymentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author ACER
 */
public class PaymentServiceTest {
    
    public PaymentServiceTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getPendingPaymentAmount method, of class PaymentService.
     */
    @Test
    public void testGetPendingPaymentAmount() {
        System.out.println("getPendingPaymentAmount");
        String bookingId = "B0001";
        PaymentService instance = new PaymentService();
        double expResult = 0.0;
        double result = instance.getPendingPaymentAmount(bookingId);
        assertEquals(expResult, result, 0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of processPayment method, of class PaymentService.
     */
    @Test
    public void testProcessPayment() {
        System.out.println("processPayment");
        Payment payment = null;
        PaymentService instance = new PaymentService();
        boolean expResult = false;
        boolean result = instance.processPayment(payment);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
    
}
