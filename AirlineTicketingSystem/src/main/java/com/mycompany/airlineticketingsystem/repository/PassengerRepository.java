package com.mycompany.airlineticketingsystem.repository;

import com.mycompany.airlineticketingsystem.model.PassengerEntity;
import java.util.Optional;

public interface PassengerRepository {
    void save(PassengerEntity passenger);
}
