package com.etlas.service;

import com.etlas.dto.TicketDto;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface TicketService {
    TicketDto initializeNewTicket();

    TicketDto adjustNewTicket(TicketDto newTicket, String addedCustomerId);

    TicketDto saveNewTicket(TicketDto newTicket);

    BindingResult validateTicket(TicketDto newTicket, BindingResult bindingResult);

    List<TicketDto> findAllTickets();
}
