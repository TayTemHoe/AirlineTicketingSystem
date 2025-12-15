package com.mycompany.airlineticketingsystem.repository;

import com.mycompany.airlineticketingsystem.model.PassengerEntity;

public interface PassengerRepository {
    void save(PassengerEntity passenger);
}
