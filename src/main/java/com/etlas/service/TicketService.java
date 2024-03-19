package com.etlas.service;

import com.etlas.dto.TicketDto;

public interface TicketService {
    TicketDto initializeNewTicket();

    TicketDto adjustNewTicket(TicketDto newTicket, String addedCustomerId);

    TicketDto saveNewTicket(TicketDto newTicket);
}
