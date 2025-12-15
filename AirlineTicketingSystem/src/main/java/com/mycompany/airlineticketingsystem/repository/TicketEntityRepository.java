package com.mycompany.airlineticketingsystem.repository;

import com.mycompany.airlineticketingsystem.model.TicketEntity;

public interface TicketEntityRepository {
    void save(TicketEntity ticket);

    String generateNewTicketId();
}
