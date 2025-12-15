package com.mycompany.airlineticketingsystem.service;

import com.mycompany.airlineticketingsystem.model.PassengerEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PassengerServiceTest {

    private PassengerService passengerService;

    @BeforeEach
    public void setUp() {
        passengerService = new PassengerService();
    }

    @Test
    public void testSavePassenger() {
        PassengerEntity passenger = new PassengerEntity("TEST_PASSPORT_001", "Test User");
        Assertions.assertDoesNotThrow(() -> passengerService.savePassenger(passenger));
    }
}
