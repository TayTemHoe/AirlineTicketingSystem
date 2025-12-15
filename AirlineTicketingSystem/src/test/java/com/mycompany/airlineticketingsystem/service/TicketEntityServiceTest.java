package com.mycompany.airlineticketingsystem.service;

import com.mycompany.airlineticketingsystem.model.TicketEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TicketEntityServiceTest {

    private TicketEntityService ticketService;

    @BeforeEach
    public void setUp() {
        ticketService = new TicketEntityService();
    }

    @Test
    public void testGenerateNewTicketId() {
        String ticketId = ticketService.generateNewTicketId();
        Assertions.assertNotNull(ticketId);
        Assertions.assertTrue(ticketId.startsWith("T"));
    }

    @Test
    public void testSaveTicket() {
        String newId = ticketService.generateNewTicketId();
        TicketEntity ticket = new TicketEntity(
                newId + "_TEST",
                "FLIGHT_TEST",
                "SEAT_TEST_ID",
                "1A",
                "PASSPORT_TEST",
                "IC_TEST");
        Assertions.assertDoesNotThrow(() -> ticketService.saveTicket(ticket));
    }
}
