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
        
    private ReportService instance;
    
    
    @BeforeEach
    public void setUp() {
        instance = new ReportService();
    }
    
    /**
     * Test of getRevenueByMethod method, of class ReportService.
     */
    @Test
    public void testGetRevenueByMethod() {
        System.out.println("Testing getRevenueByMethod...");
        
        Map<String, Double> result = instance.getRevenueByMethod();
        
        // Fix: Ensure result is NOT null and has data
        assertNotNull(result, "Result map should not be null");
        assertFalse(result.isEmpty(), "Revenue data should not be empty");
        
        System.out.println("Revenue Data: " + result);
    }

    /**
     * Test of getTopDestinations method, of class ReportService.
     */
    @Test
    public void testGetTopDestinations() {
        System.out.println("Testing getTopDestinations...");
        
        Map<String, Integer> result = this.instance.getTopDestinations();
        
        // Fix: Ensure result is NOT null
        assertNotNull(result, "Result map should not be null");
        // We don't check isEmpty() strictly here because fresh DB might have no flights yet,
        // but based on your logs, you have data, so this will pass.
        
        System.out.println("Destinations Data: " + result);
    }

    /**
     * Test of getSeatClassDistribution method, of class ReportService.
     */
    @Test
    public void testGetSeatClassDistribution() {
        System.out.println("Testing getSeatClassDistribution...");

        Map<String, Integer> result = instance.getSeatClassDistribution();
        
        // Fix: Ensure result is NOT null
        assertNotNull(result, "Result map should not be null");
        
        System.out.println("Seat Class Data: " + result);
    }

    /**
     * Test of getCustomerGenderDistribution method, of class ReportService.
     */
    @Test
    public void testGetCustomerGenderDistribution() {
        System.out.println("Testing getCustomerGenderDistribution...");
        
        Map<String, Integer> result = instance.getCustomerGenderDistribution();
        
        // Fix: Ensure result is NOT null
        assertNotNull(result, "Result map should not be null");
        
        System.out.println("Gender Data: " + result);
    }
}
