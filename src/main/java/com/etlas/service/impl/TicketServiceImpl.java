package com.etlas.service.impl;

import com.etlas.dto.*;
import com.etlas.entity.Customer;
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
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final AirLineService airLineService;
    private final SecurityService securityService;
    private final AirportService airportService;
    private final CustomerService customerService;
    private final TicketRepository repository;
    private final CardService cardService;
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
                .perchesPrice(BigDecimal.ZERO)
                .salesPrice(BigDecimal.ZERO)
                .airLine(turkishAirline)
                .fromWhere(fromWhere)
                .toWhere(toWhere)
                .boughtUser(currentUser)
                .currencyUnit(CurrencyUnits.TRY)
                .build();
    }

    @Override
    public TicketDto adjustNewTicket(TicketDto newTicket, String addedCustomerId) {
        int passengerSize = newTicket.getPassengersUI().size();
        int ticketAmount = newTicket.getTicketAmount();

        if (passengerSize >= 1 && passengerSize < ticketAmount) {
            newTicket.getPassengersUI().add(addedCustomerId);  // add new customer to passenger list
        } else if (passengerSize <= 1 && ticketAmount == 1) {
            newTicket.setPassengersUI(List.of(addedCustomerId)); // make list from new passenger
        }
        newTicket.setPayedCustomerUI(addedCustomerId); // set new customer to payer
        return newTicket;
    }

    @Override
    public TicketDto saveNewTicket(TicketDto newTicket) {
        // save new customer if added
        customerService.saveNewCustomerIfAdded(Long.parseLong(newTicket.getPayedCustomerUI()));
        prepareToSave(newTicket); // prepare to save new ticket
        calculateCustomerBalance(newTicket);  // calculate customer balance and profit
        calculateCreditCardLimit(newTicket);  // calculate credit card limit
        newTicket.setProfit(newTicket.getSalesPrice().subtract(newTicket.getPerchesPrice())); // calculate profit

        Ticket savedTicket = repository.save(mapper.convert(newTicket, new Ticket()));
        return mapper.convert(savedTicket, new TicketDto());
    }

    @Override
    public void save(TicketDto linkedTicket) {
        repository.save(mapper.convert(linkedTicket, new Ticket()));
    }

    private void prepareToSave(TicketDto newTicket) {
        LocalDateTime[] departureAndReturnDateTime = parseDateTimeRange(newTicket.getDateRangeString());
        LocalDateTime departureDate = departureAndReturnDateTime[0];
        LocalDateTime returnDateTime = departureAndReturnDateTime[1];

        newTicket.setDepartureTime(departureDate);

        setPassengers(newTicket); // set passengers from UI to DTO

        if (newTicket.isRoundTrip()) {
            newTicket.setTripType(TripType.ROUND);
            newTicket.setReturnTime(returnDateTime);
        } else newTicket.setTripType(TripType.ONEWAY);

        if (newTicket.isMultipleTicket()) {
            newTicket.setTicketType(TicketType.MULTIPLE);
        } else newTicket.setTicketType(TicketType.SINGLE);

        CustomerDto payedCustomer = customerService.findById(Long.parseLong(newTicket.getPayedCustomerUI()));
        newTicket.setPayedCustomer(payedCustomer); // set paid customer
    }

    private void setPassengers(TicketDto newTicket) {
        List<CustomerDto> passengers = newTicket.getPassengersUI().stream()
                .map(Long::parseLong).map(customerService::findById)
                .toList();
        newTicket.setPassengers(passengers);
    }

    private void calculateCustomerBalance(TicketDto newTicket) {
        CustomerDto customer = newTicket.getPayedCustomer();
        CurrencyUnits currencyUnits = newTicket.getCurrencyUnit();

        if (newTicket.getPerchesPrice() == null) newTicket.setPerchesPrice(BigDecimal.ZERO);
        if (newTicket.getSalesPrice() == null) newTicket.setSalesPrice(BigDecimal.ZERO);

        BigDecimal perches = newTicket.getPerchesPrice();
        BigDecimal sales = newTicket.getSalesPrice();

        newTicket.setProfit(sales.subtract(perches));

        if (currencyUnits.getDescription().equals("₺ TRY")) {
            BigDecimal newBalance = customer.getCustomerTRYBalance().subtract(sales);
            customer.setCustomerTRYBalance(newBalance);
            customerService.saveCustomer(customer);

        } else if (currencyUnits.getDescription().equals("$ USD")) {
            BigDecimal newBalance = customer.getCustomerUSDBalance().subtract(sales);
            customer.setCustomerUSDBalance(newBalance);
            customerService.saveCustomer(customer);
        } else {
            BigDecimal newBalance = customer.getCustomerEURBalance().subtract(sales);
            customer.setCustomerEURBalance(newBalance);
            customerService.saveCustomer(customer);
        }
    }

    private void calculateCreditCardLimit(TicketDto newTicket) {
        long paidCardId = newTicket.getPaidCard().getId();
        CardDto creditCard = cardService.findById(paidCardId);

        if (newTicket.getCurrencyUnit().equals(CurrencyUnits.TRY)) {
            BigDecimal newLimit = creditCard.getAvailableLimitTRY().subtract(newTicket.getPerchesPrice());
            creditCard.setAvailableLimitTRY(newLimit);
            cardService.saveCreditCard(creditCard);
        }

        if (newTicket.getCurrencyUnit().equals(CurrencyUnits.USD)) {
            BigDecimal newLimit = creditCard.getAvailableLimitUSD().subtract(newTicket.getPerchesPrice());
            creditCard.setAvailableLimitUSD(newLimit);
            cardService.saveCreditCard(creditCard);
        }

        if (newTicket.getCurrencyUnit().equals(CurrencyUnits.EUR)) {
            BigDecimal newLimit = creditCard.getAvailableLimitEUR().subtract(newTicket.getPerchesPrice());
            creditCard.setAvailableLimitEUR(newLimit);
            cardService.saveCreditCard(creditCard);
        }
    }

    @Override
    public BindingResult validateTicket(TicketDto newTicket, BindingResult bindingResult) {
        if (Objects.equals(newTicket.getPayedCustomerUI(), "") || Objects.equals(newTicket.getPayedCustomerUI(), "0")) {
            bindingResult.addError(new FieldError("newTicket", "payedCustomerUI", "Please select paid customer"));
        }
        if (newTicket.getPassengersUI().equals(null)){
            bindingResult.addError(new FieldError("newTicket", "passengersUI", "Please select at least one passenger"));
        }

        if (repository.existsByPnrNo(newTicket.getPnrNo())) {
            bindingResult.addError(new FieldError("updatedTicket", "pnrNo", "PNR: " + newTicket.getPnrNo() + " is already exist"));
        }
        return validateNewAndUpdatedTicket(newTicket, bindingResult);

    }

    @Override
    public BindingResult validateUpdatedTicket(TicketDto updatedTicket, BindingResult bindingResult) {

        if (repository.existsByPnrNoAndIdNot(updatedTicket.getPnrNo(), updatedTicket.getId())) {
            bindingResult.addError(new FieldError("updatedTicket", "pnrNo", "PNR: " + updatedTicket.getPnrNo() + " is already exist"));
        }
        return validateNewAndUpdatedTicket(updatedTicket, bindingResult);

    }

    private BindingResult validateNewAndUpdatedTicket(TicketDto ticket, BindingResult bindingResult) {
        LocalDateTime[] startEndDate = parseDateTimeRange(ticket.getDateRangeString());
        LocalDate departureDate = startEndDate[0].toLocalDate();
        LocalDate perchesDate = ticket.getDateOfPerches();

        if (perchesDate.isAfter(departureDate)) {
            bindingResult.addError(new FieldError("ticket", "dateOfPerches", "Date of perches: '" + ticket.getDateOfPerches() + "' couldn't be after departure date"));
        }
        if (ticket.getPassengersUI().size() > ticket.getTicketAmount()) {
            bindingResult.addError(new FieldError("ticket", "passengersUI", "Passengers couldn't be more then ticket amount"));
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

        if (tickets.isEmpty()) return List.of();
        return tickets.stream()
                .map(ticket -> mapper.convert(ticket, new TicketDto()))
                .collect(Collectors.toList());
    }

    @Override
    public TicketDto findById(long ticketId) {
        Ticket ticket = repository.findByIdAndIsDeleted(ticketId, false);
        return mapper.convert(ticket, new TicketDto());
    }

    @Override
    public TicketDto prepareTicketToUpdate(TicketDto ticketTobeUpdate) {
        if (ticketTobeUpdate.getTripType().equals(TripType.ONEWAY)) {
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
        if (ticketTobeUpdate.getTicketType().equals(TicketType.SINGLE)) {
            ticketTobeUpdate.setSingleTicket(true);
            ticketTobeUpdate.setMultipleTicket(false);
        } else {
            ticketTobeUpdate.setSingleTicket(false);
            ticketTobeUpdate.setMultipleTicket(true);
        }
        // set passengers
        List<String> passengersUI = ticketTobeUpdate.getPassengers().stream()
                .map(ticket -> String.valueOf(ticket.getId()))
                .toList();
        ticketTobeUpdate.setPassengersUI(passengersUI);

        // set paid customer
        String payedCustomer = String.valueOf(ticketTobeUpdate.getPayedCustomer().getId());
        ticketTobeUpdate.setPayedCustomerUI(payedCustomer);

        return ticketTobeUpdate;
    }

    @Override
    public TicketDto saveUpdatedTicket(TicketDto updatedTicket) {
        // save new customer if added
        customerService.saveNewCustomerIfAdded(Long.parseLong(updatedTicket.getPayedCustomerUI()));

        resetOldPaidCustomerBalance(updatedTicket); // reset old paid customer balance
        resetOldCreditCardLimit(updatedTicket); // reset old credit card limit

        prepareToSave(updatedTicket); // prepare to save updated ticket
        calculateCustomerBalance(updatedTicket);  // calculate customer balance
        calculateCreditCardLimit(updatedTicket);  // calculate credit card limit
        updatedTicket.setProfit(updatedTicket.getSalesPrice().subtract(updatedTicket.getPerchesPrice())); // calculate profit

        Ticket savedTicket = repository.save(mapper.convert(updatedTicket, new Ticket()));

        return mapper.convert(savedTicket, new TicketDto());
    }


    private void resetOldPaidCustomerBalance(TicketDto updatedTicket) {
        TicketDto oldTicket = findById(updatedTicket.getId());

        CustomerDto oldPidCustomer = oldTicket.getPayedCustomer();
        CurrencyUnits oldCurrencyUnits = oldTicket.getCurrencyUnit();

        BigDecimal oldSales = oldTicket.getSalesPrice();

        if (oldCurrencyUnits.getDescription().equals("₺ TRY")) {
            BigDecimal newBalance = oldPidCustomer.getCustomerTRYBalance().add(oldSales);
            oldPidCustomer.setCustomerTRYBalance(newBalance);
            customerService.saveCustomer(oldPidCustomer);

        } else if (oldCurrencyUnits.getDescription().equals("$ USD")) {
            BigDecimal newBalance = oldPidCustomer.getCustomerUSDBalance().add(oldSales);
            oldPidCustomer.setCustomerUSDBalance(newBalance);
            customerService.saveCustomer(oldPidCustomer);
        } else {
            BigDecimal newBalance = oldPidCustomer.getCustomerEURBalance().add(oldSales);
            oldPidCustomer.setCustomerEURBalance(newBalance);
            customerService.saveCustomer(oldPidCustomer);
        }
    }

    private void resetOldCreditCardLimit(TicketDto updatedTicket) {

        TicketDto oldTicket = findById(updatedTicket.getId());
        CardDto oldCreditCard = oldTicket.getPaidCard();

        if (oldTicket.getCurrencyUnit().equals(CurrencyUnits.TRY)) {
            BigDecimal newLimit = oldCreditCard.getAvailableLimitTRY().add(oldTicket.getPerchesPrice());
            oldCreditCard.setAvailableLimitTRY(newLimit);
            cardService.saveCreditCard(oldCreditCard);
        }

        if (oldTicket.getCurrencyUnit().equals(CurrencyUnits.USD)) {
            BigDecimal newLimit = oldCreditCard.getAvailableLimitUSD().add(oldTicket.getPerchesPrice());
            oldCreditCard.setAvailableLimitUSD(newLimit);
            cardService.saveCreditCard(oldCreditCard);
        }

        if (oldTicket.getCurrencyUnit().equals(CurrencyUnits.EUR)) {
            BigDecimal newLimit = oldCreditCard.getAvailableLimitEUR().add(oldTicket.getPerchesPrice());
            oldCreditCard.setAvailableLimitEUR(newLimit);
            cardService.saveCreditCard(oldCreditCard);
        }
    }

    @Override
    public boolean isCustomerHasTickets(Customer customer) {
        return repository.existsByPayedCustomerOrPassengersContainingAndIsDeleted(customer.getId(), customer.getId(), false);
    }

    @Override
    public boolean isUserBoughtTicket(String userName) {
        return repository.existsByBoughtUser_UserNameAndIsDeleted(userName,  false);
    }

    @Override
    public boolean deleteTicket(long ticketId) {
        Ticket ticketToBeDeleted = repository.findById(ticketId).orElseThrow(NoSuchElementException::new);
        ticketToBeDeleted.setPnrNo(ticketToBeDeleted.getPnrNo() + "_" + LocalDateTime.now());
        ticketToBeDeleted.setDeleted(true);
        repository.save(ticketToBeDeleted);
        return true;
    }

    @Override
    public boolean isCardUsedInAnyTicket(String cardId) {
        long id = Long.parseLong(cardId);
        return repository.existsByPayedCardIdAndIsDeleted(id, false);
    }

    @Override
    public List<TicketDto> findTicketsByCustomerId(long customerId) {
        List<Ticket> ticketList =  repository.findAllByPayedCustomer_IdAndIsDeleted(customerId, false);
        if (!ticketList.isEmpty()) {
            return ticketList.stream()
                .map(ticket -> mapper.convert(ticket, new TicketDto()))
                .toList();
        }
        return List.of();
    }

    @Override
    public BigDecimal getTicketTRYTotalPerches(String time) {
        BigDecimal result;
        if (Objects.equals(time, "LastMonth")) {
            result = repository.getTicketTotalPerchesByMonth(CurrencyUnits.TRY, LocalDate.now().getMonth().minus(1).getValue(),false);
        }
        else if (Objects.equals(time, "thisYear")) {

            result = repository.getTicketTotalPerchesByYear(CurrencyUnits.TRY, LocalDate.now().getYear(),false);
        }
        else if (Objects.equals(time, "lastYear")) {
            return repository.getTicketTotalPerchesByYear(CurrencyUnits.TRY, LocalDate.now().getYear()-1,false);
        }
        else if (Objects.equals(time, "all")) {
            result = repository.getTicketTotalPerches(CurrencyUnits.TRY,false);
        }
        else {result = repository.getTicketTotalPerchesByMonth(CurrencyUnits.TRY, LocalDate.now().getMonthValue(),false);}

        return result == null ? BigDecimal.ZERO : result;
    }

    @Override
    public BigDecimal getTicketUSDTotalPerches(String time) {
        BigDecimal result;
        if (Objects.equals(time, "LastMonth")) {
            result = repository.getTicketTotalPerchesByMonth(CurrencyUnits.USD, LocalDate.now().getMonth().minus(1).getValue(),false);
        }
        else if (Objects.equals(time, "thisYear")) {

            result = repository.getTicketTotalPerchesByYear(CurrencyUnits.USD, LocalDate.now().getYear(),false);
        }
        else if (Objects.equals(time, "lastYear")) {
            result = repository.getTicketTotalPerchesByYear(CurrencyUnits.USD, LocalDate.now().getYear()-1,false);
        }
        else if (Objects.equals(time, "all")) {
            result = repository.getTicketTotalPerches(CurrencyUnits.USD,false);
        }
        else {result = repository.getTicketTotalPerchesByMonth(CurrencyUnits.USD, LocalDate.now().getMonthValue(),false);}

        return result == null ? BigDecimal.ZERO : result;
    }

    @Override
    public BigDecimal getTicketEURTotalPerches(String time) {
        BigDecimal result;
        if (Objects.equals(time, "LastMonth")) {
            result = repository.getTicketTotalPerchesByMonth(CurrencyUnits.EUR, LocalDate.now().getMonth().minus(1).getValue(),false);
        } else if (Objects.equals(time, "thisYear")) {

            result = repository.getTicketTotalPerchesByYear(CurrencyUnits.EUR, LocalDate.now().getYear(),false);
        } else if (Objects.equals(time, "lastYear")) {
            result = repository.getTicketTotalPerchesByYear(CurrencyUnits.EUR, LocalDate.now().getYear() - 1,false);
        } else if (Objects.equals(time, "all")) {
            result = repository.getTicketTotalPerches(CurrencyUnits.EUR,false);
        }
        else {result = repository.getTicketTotalPerchesByMonth(CurrencyUnits.EUR, LocalDate.now().getMonthValue(),false);}

        return result == null ? BigDecimal.ZERO : result;
    }

    @Override
    public BigDecimal getTicketTRYTotalSales(String time) {
        BigDecimal result;
        if (Objects.equals(time, "LastMonth")) {
            result = repository.getTicketTotalSalesByMonth(CurrencyUnits.TRY, LocalDate.now().getMonth().minus(1).getValue(),false);
        }
        else if (Objects.equals(time, "thisYear")) {

            result = repository.getTicketTotalSalesByYear(CurrencyUnits.TRY, LocalDate.now().getYear(),false);
        }
        else if (Objects.equals(time, "lastYear")) {
            return repository.getTicketTotalSalesByYear(CurrencyUnits.TRY, LocalDate.now().getYear()-1,false);
        }
        else if (Objects.equals(time, "all")) {
            result = repository.getTicketTotalSales(CurrencyUnits.TRY,false);
        }
        else {result = repository.getTicketTotalSalesByMonth(CurrencyUnits.TRY, LocalDate.now().getMonthValue(),false);}

        return result == null ? BigDecimal.ZERO : result;
    }

    @Override
    public BigDecimal getTicketUSDTotalSales(String time) {
        BigDecimal result;
        if (Objects.equals(time, "LastMonth")) {
            result = repository.getTicketTotalSalesByMonth(CurrencyUnits.USD, LocalDate.now().getMonth().minus(1).getValue(),false);
        }
        else if (Objects.equals(time, "thisYear")) {

            result = repository.getTicketTotalSalesByYear(CurrencyUnits.USD, LocalDate.now().getYear(),false);
        }
        else if (Objects.equals(time, "lastYear")) {
            result = repository.getTicketTotalSalesByYear(CurrencyUnits.USD, LocalDate.now().getYear()-1,false);
        }
        else if (Objects.equals(time, "all")) {
            result = repository.getTicketTotalSales(CurrencyUnits.USD,false);
        }
        else {result = repository.getTicketTotalSalesByMonth(CurrencyUnits.USD, LocalDate.now().getMonthValue(),false);}

        return result == null ? BigDecimal.ZERO : result;
    }

    @Override
    public BigDecimal getTicketEURTotalSales(String time) {
        BigDecimal result;
        if (Objects.equals(time, "LastMonth")) {
            result = repository.getTicketTotalSalesByMonth(CurrencyUnits.EUR, LocalDate.now().getMonth().minus(1).getValue(),false);
        } else if (Objects.equals(time, "thisYear")) {

            result = repository.getTicketTotalSalesByYear(CurrencyUnits.EUR, LocalDate.now().getYear(),false);
        } else if (Objects.equals(time, "lastYear")) {
            result = repository.getTicketTotalSalesByYear(CurrencyUnits.EUR, LocalDate.now().getYear() - 1,false);
        } else if (Objects.equals(time, "all")) {
            result = repository.getTicketTotalSales(CurrencyUnits.EUR,false);
        }
        else {result = repository.getTicketTotalSalesByMonth(CurrencyUnits.EUR, LocalDate.now().getMonthValue(),false);}

        return result == null ? BigDecimal.ZERO : result;
    }


    @Override
    public BigDecimal getTicketTRYTotalProfit(String time) {
        BigDecimal result;
        if (Objects.equals(time, "LastMonth")) {
            result = repository.getTicketTotalProfitByMonth(CurrencyUnits.TRY, LocalDate.now().getMonth().minus(1).getValue(),false);
        }
        else if (Objects.equals(time, "thisYear")) {

            result = repository.getTicketTotalProfitByYear(CurrencyUnits.TRY, LocalDate.now().getYear(),false);
        }
        else if (Objects.equals(time, "lastYear")) {
            return repository.getTicketTotalProfitByYear(CurrencyUnits.TRY, LocalDate.now().getYear()-1,false);
        }
        else if (Objects.equals(time, "all")) {
            result = repository.getTicketTotalProfit(CurrencyUnits.TRY,false);
        }
        else {result = repository.getTicketTotalProfitByMonth(CurrencyUnits.TRY, LocalDate.now().getMonthValue(),false);}

        return result == null ? BigDecimal.ZERO : result;
    }

    @Override
    public BigDecimal getTicketUSDTotalProfit(String time) {
        BigDecimal result;
        if (Objects.equals(time, "LastMonth")) {
            result = repository.getTicketTotalProfitByMonth(CurrencyUnits.USD, LocalDate.now().getMonth().minus(1).getValue(),false);
        }
        else if (Objects.equals(time, "thisYear")) {

            result = repository.getTicketTotalProfitByYear(CurrencyUnits.USD, LocalDate.now().getYear(),false);
        }
        else if (Objects.equals(time, "lastYear")) {
            result = repository.getTicketTotalProfitByYear(CurrencyUnits.USD, LocalDate.now().getYear()-1,false);
        }
        else if (Objects.equals(time, "all")) {
            result = repository.getTicketTotalProfit(CurrencyUnits.USD,false);
        }
        else {result = repository.getTicketTotalProfitByMonth(CurrencyUnits.USD, LocalDate.now().getMonthValue(),false);}

        return result == null ? BigDecimal.ZERO : result;
    }

    @Override
    public BigDecimal getTicketEURTotalProfit(String time) {
        BigDecimal result;
        if (Objects.equals(time, "LastMonth")) {
            result = repository.getTicketTotalProfitByMonth(CurrencyUnits.EUR, LocalDate.now().getMonth().minus(1).getValue(),false);
        } else if (Objects.equals(time, "thisYear")) {

            result = repository.getTicketTotalProfitByYear(CurrencyUnits.EUR, LocalDate.now().getYear(),false);
        } else if (Objects.equals(time, "lastYear")) {
            result = repository.getTicketTotalProfitByYear(CurrencyUnits.EUR, LocalDate.now().getYear() - 1,false);
        } else if (Objects.equals(time, "all")) {
            result = repository.getTicketTotalProfit(CurrencyUnits.EUR,false);
        }
        else {result = repository.getTicketTotalProfitByMonth(CurrencyUnits.EUR, LocalDate.now().getMonthValue(),false);}

        return result == null ? BigDecimal.ZERO : result;
    }

    @Override
    public int getTotalTRYPerchesTicket(String time) {
        Integer result;
        if (Objects.equals(time, "LastMonth")) {
            result = repository.getTotalPerchesTicketByMonth(CurrencyUnits.TRY, LocalDate.now().getMonth().minus(1).getValue(),false);
        }
        else if (Objects.equals(time, "thisYear")) {

            result = repository.getTotalPerchesTicketByYear(CurrencyUnits.TRY, LocalDate.now().getYear(),false);
        }
        else if (Objects.equals(time, "lastYear")) {
            result =  repository.getTotalPerchesTicketByYear(CurrencyUnits.TRY, LocalDate.now().getYear()-1,false);
        }
        else if (Objects.equals(time, "all")) {
            result = repository.getTotalPerchesTicket(CurrencyUnits.TRY,false);
        }
        else {result = repository.getTotalPerchesTicketByMonth(CurrencyUnits.TRY, LocalDate.now().getMonthValue(),false);}

        return result == null ? 0 : result;
    }

    @Override
    public int getTotalUSDPerchesTicket(String time) {
        Integer result;
        if (Objects.equals(time, "LastMonth")) {
            result = repository.getTotalPerchesTicketByMonth(CurrencyUnits.USD, LocalDate.now().getMonth().minus(1).getValue(),false);
        }
        else if (Objects.equals(time, "thisYear")) {

            result = repository.getTotalPerchesTicketByYear(CurrencyUnits.USD, LocalDate.now().getYear(),false);
        }
        else if (Objects.equals(time, "lastYear")) {
            result =  repository.getTotalPerchesTicketByYear(CurrencyUnits.USD, LocalDate.now().getYear()-1,false);
        }
        else if (Objects.equals(time, "all")) {
            result = repository.getTotalPerchesTicket(CurrencyUnits.USD,false);
        }
        else {result = repository.getTotalPerchesTicketByMonth(CurrencyUnits.USD, LocalDate.now().getMonthValue(),false);}

        return result == null ? 0 : result;
    }

    @Override
    public int getTotalEURPerchesTicket(String time) {
        Integer result;
        if (Objects.equals(time, "LastMonth")) {
            result = repository.getTotalPerchesTicketByMonth(CurrencyUnits.EUR, LocalDate.now().getMonth().minus(1).getValue(),false);
        }
        else if (Objects.equals(time, "thisYear")) {

            result = repository.getTotalPerchesTicketByYear(CurrencyUnits.EUR, LocalDate.now().getYear(),false);
        }
        else if (Objects.equals(time, "lastYear")) {
            result =  repository.getTotalPerchesTicketByYear(CurrencyUnits.EUR, LocalDate.now().getYear()-1,false);
        }
        else if (Objects.equals(time, "all")) {
            result = repository.getTotalPerchesTicket(CurrencyUnits.EUR,false);
        }
        else {result = repository.getTotalPerchesTicketByMonth(CurrencyUnits.EUR, LocalDate.now().getMonthValue(),false);}

        return result == null ? 0 : result;
    }
}
