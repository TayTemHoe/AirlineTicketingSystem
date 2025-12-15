/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.airlineticketingsystem;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ACER
 */
public class PaymentServiceTest {
    
    public PaymentServiceTest() {
    }

    @org.junit.BeforeClass
    public static void setUpClass() throws Exception {
    }

    @org.junit.AfterClass
    public static void tearDownClass() throws Exception {
    }

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getPendingPaymentAmount method, of class PaymentService.
     */
    @org.junit.Test
    public void testGetPendingPaymentAmount() {
        System.out.println("getPendingPaymentAmount");
        int bookingId = 0;
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
    @org.junit.Test
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
