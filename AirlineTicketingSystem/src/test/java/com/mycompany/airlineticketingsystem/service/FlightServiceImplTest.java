/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.airlineticketingsystem.service;

import com.mycompany.airlineticketingsystem.model.Flight;
import com.mycompany.airlineticketingsystem.model.Plane;
import com.mycompany.airlineticketingsystem.model.Seat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Tay Tem Hoe
 */
public class FlightServiceImplTest {
    
    public FlightServiceImplTest() {
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
     * Test of getAllFlights method, of class FlightServiceImpl.
     */
    @Test
    public void testGetAllFlights() {
        System.out.println("getAllFlights");
        FlightServiceImpl instance = new FlightServiceImpl();
        List<Flight> expResult = null;
        List<Flight> result = instance.getAllFlights();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of refreshCache method, of class FlightServiceImpl.
     */
    @Test
    public void testRefreshCache() {
        System.out.println("refreshCache");
        FlightServiceImpl instance = new FlightServiceImpl();
        instance.refreshCache();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createFlight method, of class FlightServiceImpl.
     */
    @Test
    public void testCreateFlight() {
        System.out.println("createFlight");
        Flight flight = null;
        Plane plane = null;
        FlightServiceImpl instance = new FlightServiceImpl();
        instance.createFlight(flight, plane);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteFlight method, of class FlightServiceImpl.
     */
    @Test
    public void testDeleteFlight() {
        System.out.println("deleteFlight");
        String flightId = "";
        FlightServiceImpl instance = new FlightServiceImpl();
        instance.deleteFlight(flightId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateFlight method, of class FlightServiceImpl.
     */
    @Test
    public void testUpdateFlight() {
        System.out.println("updateFlight");
        Flight flight = null;
        FlightServiceImpl instance = new FlightServiceImpl();
        instance.updateFlight(flight);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchFlights method, of class FlightServiceImpl.
     */
    @Test
    public void testSearchFlights() {
        System.out.println("searchFlights");
        String from = "";
        String to = "";
        LocalDate date = null;
        FlightServiceImpl instance = new FlightServiceImpl();
        List<Flight> expResult = null;
        List<Flight> result = instance.searchFlights(from, to, date);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSeatsForFlight method, of class FlightServiceImpl.
     */
    @Test
    public void testGetSeatsForFlight() {
        System.out.println("getSeatsForFlight");
        String flightId = "";
        FlightServiceImpl instance = new FlightServiceImpl();
        List<Seat> expResult = null;
        List<Seat> result = instance.getSeatsForFlight(flightId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllPlanes method, of class FlightServiceImpl.
     */
    @Test
    public void testGetAllPlanes() {
        System.out.println("getAllPlanes");
        FlightServiceImpl instance = new FlightServiceImpl();
        List<Plane> expResult = null;
        List<Plane> result = instance.getAllPlanes();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFlightById method, of class FlightServiceImpl.
     */
    @Test
    public void testGetFlightById() {
        System.out.println("getFlightById");
        String flightId = "";
        FlightServiceImpl instance = new FlightServiceImpl();
        Optional<Flight> expResult = null;
        Optional<Flight> result = instance.getFlightById(flightId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
