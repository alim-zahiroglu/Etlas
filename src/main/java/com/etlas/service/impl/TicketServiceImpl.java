package com.etlas.service.impl;

import com.etlas.dto.AirLineDto;
import com.etlas.dto.AirportDto;
import com.etlas.dto.TicketDto;
import com.etlas.dto.UserDto;
import com.etlas.entity.User;
import com.etlas.enums.CurrencyUnits;
import com.etlas.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final AirLineService airLineService;
    private final SecurityService securityService;
    private final AirportService airportService;
    private final CustomerService customerService;
    @Override
    public TicketDto initializeNewTicket() {
        AirLineDto turkishAirline = airLineService.findByName("Turkish Airlines");
        UserDto currentUser = securityService.getLoggedInUser();
        AirportDto fromWhere = airportService.findByIataCode("IST");
        AirportDto toWhere = airportService.findByIataCode("JED");
        return TicketDto.builder()
                .singleTicket(true)
                .oneWayTrip(true)
                .airLine(turkishAirline)
                .fromWhere(fromWhere)
                .toWhere(toWhere)
                .boughtUser(currentUser)
                .receivedUser(currentUser)
                .currencyUnit(CurrencyUnits.TRY)
                .build();
    }

    @Override
    public TicketDto adjustNewTicket(TicketDto newTicket, String addedCustomerId) {
        int passengerSize = newTicket.getPassengersUI().size();
        int ticketAmount = newTicket.getTicketAmount();

        if (passengerSize >=1 && passengerSize < ticketAmount){
            newTicket.getPassengersUI().add(addedCustomerId);  // add new customer to passenger list
        }else if (passengerSize <= 1 && ticketAmount == 1){
            newTicket.setPassengersUI(List.of(addedCustomerId)); // make list from new passenger
        }
        newTicket.setPayedCustomer(customerService.findById(Long.parseLong(addedCustomerId))); // set new customer to payer
        return newTicket;
    }

    @Override
    public TicketDto saveNewTicket(TicketDto newTicket) {

        return null;
    }
}
