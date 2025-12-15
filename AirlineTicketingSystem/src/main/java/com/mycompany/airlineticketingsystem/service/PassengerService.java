package com.mycompany.airlineticketingsystem.service;

import com.mycompany.airlineticketingsystem.model.PassengerEntity;
import com.mycompany.airlineticketingsystem.repository.PassengerRepository;
import com.mycompany.airlineticketingsystem.repository.PassengerRepositoryImpl;
import java.util.Optional;

public class PassengerService {

    private final PassengerRepository repository;

    public PassengerService() {
        this.repository = new PassengerRepositoryImpl();
    }

    public void savePassenger(PassengerEntity passenger) {
        repository.save(passenger);
    }
    
}
