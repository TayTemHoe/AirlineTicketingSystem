/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.airlineticketingsystem.service;

import com.mycompany.airlineticketingsystem.service.ReportService;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

/**
 *
 * @author ACER
 */
public class ReportServiceTest {
    
    public ReportServiceTest() {
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
     * Test of getRevenueByMethod method, of class ReportService.
     */
    @Test
    public void testGetRevenueByMethod() {
        System.out.println("getRevenueByMethod");
        ReportService instance = new ReportService();
        Map<String, Double> expResult = null;
        Map<String, Double> result = instance.getRevenueByMethod();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTopDestinations method, of class ReportService.
     */
    @Test
    public void testGetTopDestinations() {
        System.out.println("getTopDestinations");
        ReportService instance = new ReportService();
        Map<String, Integer> expResult = null;
        Map<String, Integer> result = instance.getTopDestinations();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSeatClassDistribution method, of class ReportService.
     */
    @Test
    public void testGetSeatClassDistribution() {
        System.out.println("getSeatClassDistribution");
        ReportService instance = new ReportService();
        Map<String, Integer> expResult = null;
        Map<String, Integer> result = instance.getSeatClassDistribution();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCustomerGenderDistribution method, of class ReportService.
     */
    @Test
    public void testGetCustomerGenderDistribution() {
        System.out.println("getCustomerGenderDistribution");
        ReportService instance = new ReportService();
        Map<String, Integer> expResult = null;
        Map<String, Integer> result = instance.getCustomerGenderDistribution();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
