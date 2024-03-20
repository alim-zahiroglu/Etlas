package com.etlas.service.impl;

import com.etlas.dto.*;
import com.etlas.enums.CurrencyUnits;
import com.etlas.repository.TicketRepository;
import com.etlas.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final AirLineService airLineService;
    private final SecurityService securityService;
    private final AirportService airportService;
    private final CustomerService customerService;
    private final TicketRepository repository;
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
        newTicket.setPayedCustomerUI(addedCustomerId); // set new customer to payer
        return newTicket;
    }

    @Override
    public TicketDto saveNewTicket(TicketDto newTicket) {

        return null;
    }

    @Override
    public BindingResult validateTicket(TicketDto newTicket, BindingResult bindingResult) {
        LocalDateTime[] startEndDate = parseDateTimeRange(newTicket.getDateRangeString());
        LocalDate departureDate = startEndDate[0].toLocalDate();
        LocalDate perchesDate = newTicket.getDateOfPerches();

        if (repository.existsByPnrNo(newTicket.getPnrNo())) {
            bindingResult.addError(new FieldError("newTicket", "pnrNo", "this PNR already exist"));
        }
        if (perchesDate.isAfter(departureDate)){
            bindingResult.addError(new FieldError("newTicket", "dateOfPerches", "Date of perches couldn't be after departure date"));
        }
        if (newTicket.getPassengersUI().size() > newTicket.getTicketAmount()){
            bindingResult.addError(new FieldError("newTicket", "passengersUI", "Passengers couldn't be more then ticket amount"));
        }

    return bindingResult;
    }

    private LocalDateTime[] parseDateTimeRange(String dateTimeRange) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String[] parts = dateTimeRange.split(" - ");

        if (parts.length == 1) {
            LocalDateTime dateTime = LocalDateTime.parse(parts[0], formatter);
            return new LocalDateTime[]{dateTime, dateTime};
        } else if (parts.length == 2) {
            LocalDateTime startDateTime = LocalDateTime.parse(parts[0], formatter);
            LocalDateTime endDateTime = LocalDateTime.parse(parts[1], formatter);
            return new LocalDateTime[]{startDateTime, endDateTime};
        } else {
            throw new IllegalArgumentException("Invalid date-time range format");
        }
    }
}
