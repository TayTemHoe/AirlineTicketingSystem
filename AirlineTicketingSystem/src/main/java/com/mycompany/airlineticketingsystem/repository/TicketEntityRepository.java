package com.mycompany.airlineticketingsystem.repository;

import com.mycompany.airlineticketingsystem.model.TicketEntity;
import java.util.List;

public interface TicketEntityRepository {
    void save(TicketEntity ticket);

    String generateNewTicketId();
 
    // NEW: Find tickets for a specific customer
    List<TicketEntity> findByCustomerIc(String icNo);
}
