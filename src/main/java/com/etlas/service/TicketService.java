package com.etlas.service;

import com.etlas.dto.TicketDto;
import com.etlas.entity.Customer;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.List;

public interface TicketService {
    TicketDto initializeNewTicket();

    TicketDto adjustNewTicket(TicketDto newTicket, String addedCustomerId);

    TicketDto saveNewTicket(TicketDto newTicket);

    BindingResult validateTicket(TicketDto newTicket, BindingResult bindingResult);

    List<TicketDto> findAllTickets();

    TicketDto findById(long ticketId);

    TicketDto prepareTicketToUpdate(TicketDto ticketTobeUpdate);

    BindingResult validateUpdatedTicket(TicketDto updatedTicket, BindingResult bindingResult);

    TicketDto saveUpdatedTicket(TicketDto updatedTicket);

    boolean isCustomerHasTickets(Customer customer);

    boolean isUserBoughtTicket(String userName);

    boolean deleteTicket(long ticketId);

    boolean isCardUsedInAnyTicket(String cardId);

    void save(TicketDto linkedTicket);

    List<TicketDto> findTicketsByCustomerId(long customerId);

    BigDecimal getTicketTRYTotalPerches();

    BigDecimal getTicketUSDTotalPerches();

    BigDecimal getTicketEURTotalPerches();
}
