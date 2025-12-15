package com.mycompany.airlineticketingsystem.service;

import com.mycompany.airlineticketingsystem.model.TicketEntity;
import com.mycompany.airlineticketingsystem.repository.TicketEntityRepository;
import com.mycompany.airlineticketingsystem.repository.TicketEntityRepositoryImpl;
import java.util.List;

public class TicketEntityService {

    private final TicketEntityRepository repository;

    public TicketEntityService() {
        this.repository = new TicketEntityRepositoryImpl();
    }

    public void saveTicket(TicketEntity ticket) {
        repository.save(ticket);
    }

    public String generateNewTicketId() {
        return repository.generateNewTicketId();
    }
    
    // NEW
    public List<TicketEntity> getTicketsByCustomer(String icNo) {
        return repository.findByCustomerIc(icNo);
    }
}
