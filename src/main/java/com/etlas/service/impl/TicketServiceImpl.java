package com.etlas.service.impl;

import com.etlas.dto.*;
import com.etlas.entity.Ticket;
import com.etlas.enums.CurrencyUnits;
import com.etlas.enums.TicketType;
import com.etlas.enums.TripType;
import com.etlas.mapper.MapperUtil;
import com.etlas.repository.TicketRepository;
import com.etlas.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final AirLineService airLineService;
    private final SecurityService securityService;
    private final AirportService airportService;
    private final CustomerService customerService;
    private final TicketRepository repository;
    private final MapperUtil mapper;
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
        prepareToSave(newTicket);
        Ticket savedTicket = repository.save(mapper.convert(newTicket, new Ticket()));
        return mapper.convert(savedTicket, new TicketDto());
    }

    private void prepareToSave(TicketDto newTicket) {
        LocalDateTime[] departureAndReturnDateTime = parseDateTimeRange(newTicket.getDateRangeString());
        LocalDateTime departureDate = departureAndReturnDateTime[0];
        LocalDateTime returnDateTime = departureAndReturnDateTime[1];

        newTicket.setDepartureTime(departureDate);
        setPassengers(newTicket);

        if (newTicket.isRoundTrip()){
            newTicket.setTripType(TripType.ROUND);
            newTicket.setReturnTime(returnDateTime);
        }else newTicket.setTripType(TripType.ONEWAY);

        if (newTicket.isMultipleTicket()){
            newTicket.setTicketType(TicketType.MULTIPLE);
        }else newTicket.setTicketType(TicketType.SINGLE);

        newTicket.setPayedCustomer(customerService.findById(Long.parseLong(newTicket.getPayedCustomerUI())));

        calculateCustomerBalanceAndCreditCardLimitsAndProfit(newTicket);
        saveBalanceRecord(newTicket);
    }

    private void setPassengers(TicketDto newTicket){
        List<CustomerDto> passengers = newTicket.getPassengersUI().stream()
                .map(Long::parseLong).map(customerService::findById)
                .toList();
        newTicket.setPassengers(passengers);

    }
    private void calculateCustomerBalanceAndCreditCardLimitsAndProfit (TicketDto newTicket) {
        long payedCustomerId = Long.parseLong(newTicket.getPayedCustomerUI());
        CustomerDto customer = customerService.findById(payedCustomerId);
        CurrencyUnits currencyUnits = newTicket.getCurrencyUnit();

        if (newTicket.getPerchesPrice() == null) newTicket.setPerchesPrice(BigDecimal.ZERO);
        if (newTicket.getSalesPrice() == null) newTicket.setSalesPrice(BigDecimal.ZERO);
        if (newTicket.getPayedAmount() == null) newTicket.setPayedAmount(BigDecimal.ZERO);

        BigDecimal perches = newTicket.getPerchesPrice();
        BigDecimal sales = newTicket.getSalesPrice();
        BigDecimal payed = newTicket.getPayedAmount();
        BigDecimal unPayed = sales.subtract(payed);

        newTicket.setProfit(sales.subtract(perches));

        if (currencyUnits.getDescription().equals("₺ TRY")){
            BigDecimal newBalance = customer.getCustomerTRYBalance().subtract(unPayed);
            customer.setCustomerTRYBalance(newBalance);
            customerService.saveNewCustomer(customer);

        } else if (currencyUnits.getDescription().equals("$ USD")) {
            BigDecimal newBalance = customer.getCustomerUSDBalance().subtract(unPayed);
            customer.setCustomerUSDBalance(newBalance);
            customerService.saveNewCustomer(customer);
        }else {
            BigDecimal newBalance = customer.getCustomerEURBalance().subtract(unPayed);
            customer.setCustomerEURBalance(newBalance);
            customerService.saveNewCustomer(customer);
        }
    }

    private void saveBalanceRecord(TicketDto newTicket) {
        if (newTicket.getPayedAmount().compareTo(BigDecimal.ZERO)>0){

//          TODO  saveNewBalanceRecord();
            System.out.println("********************** -> new balance record is saved");
        }
    }

    @Override
    public BindingResult validateTicket(TicketDto newTicket, BindingResult bindingResult) {

        if (repository.existsByPnrNo(newTicket.getPnrNo())) {
            bindingResult.addError(new FieldError("updatedTicket", "pnrNo", "PNR: " + newTicket.getPnrNo() + " is already exist"));
        }
        return validateNewAndUpdatedTicket(newTicket, bindingResult);

    }
    @Override
    public BindingResult validateUpdatedTicket(TicketDto updatedTicket, BindingResult bindingResult) {

        if (repository.existsByPnrNoAndIdNot(updatedTicket.getPnrNo(),updatedTicket.getId())){
            bindingResult.addError(new FieldError("updatedTicket", "pnrNo", "PNR: " + updatedTicket.getPnrNo() + " is already exist"));
        }
        return validateNewAndUpdatedTicket(updatedTicket, bindingResult);

    }
    private BindingResult validateNewAndUpdatedTicket(TicketDto ticket, BindingResult bindingResult) {
        LocalDateTime[] startEndDate = parseDateTimeRange(ticket.getDateRangeString());
        LocalDate departureDate = startEndDate[0].toLocalDate();
        LocalDate perchesDate = ticket.getDateOfPerches();

        if (perchesDate.isAfter(departureDate)){
            bindingResult.addError(new FieldError("updatedTicket", "dateOfPerches", "Date of perches: '" + ticket.getDateOfPerches() + "' couldn't be after departure date"));
        }
        if (ticket.getPassengersUI().size() > ticket.getTicketAmount()){
            bindingResult.addError(new FieldError("updatedTicket", "passengersUI", "Passengers couldn't be more then ticket amount"));
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

    @Override
    public List<TicketDto> findAllTickets() {
        List<Ticket> tickets = repository.findAllByIsDeletedOrderByLastUpdateDateTimeDesc(false);

        return tickets.stream()
                .map(ticket -> mapper.convert(ticket, new TicketDto()))
                .collect(Collectors.toList());
    }

    @Override
    public TicketDto findById(long ticketId) {
        Ticket ticket = repository.findByIdAndIsDeleted(ticketId,false);
        return mapper.convert(ticket, new TicketDto());
    }

    @Override
    public TicketDto prepareTicketToUpdate(TicketDto ticketTobeUpdate) {
        if (ticketTobeUpdate.getTripType().equals(TripType.ONEWAY)){
            ticketTobeUpdate.setOneWayTrip(true);
            ticketTobeUpdate.setRoundTrip(false);
            // set departure time string

            LocalDateTime departure = ticketTobeUpdate.getDepartureTime();
            String departureTime = departure.format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"));
            ticketTobeUpdate.setDateRangeString(departureTime);
        } else {
            ticketTobeUpdate.setOneWayTrip(false);
            ticketTobeUpdate.setRoundTrip(true);
            // set departure time string
            LocalDateTime departure = ticketTobeUpdate.getDepartureTime();
            LocalDateTime returnTime = ticketTobeUpdate.getReturnTime();

            String departureTimeString = departure.format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"));
            String returnTimeString = returnTime.format(DateTimeFormatter.ofPattern("dd/MM/yy HH:mm"));

            ticketTobeUpdate.setDateRangeString(departureTimeString + " - " + returnTimeString);
        }
        if (ticketTobeUpdate.getTicketType().equals(TicketType.SINGLE)){
            ticketTobeUpdate.setSingleTicket(true);
            ticketTobeUpdate.setMultipleTicket(false);
        }else {
            ticketTobeUpdate.setSingleTicket(false);
            ticketTobeUpdate.setMultipleTicket(true);
        }
        // set passengers
        List<String> passengersUI = ticketTobeUpdate.getPassengers().stream()
                .map(ticket->String.valueOf(ticket.getId()))
                .toList();
        ticketTobeUpdate.setPassengersUI(passengersUI);

        // set paid customer
        String payedCustomer = String.valueOf(ticketTobeUpdate.getPayedCustomer().getId());
        ticketTobeUpdate.setPayedCustomerUI(payedCustomer);

        return ticketTobeUpdate;
    }
}
