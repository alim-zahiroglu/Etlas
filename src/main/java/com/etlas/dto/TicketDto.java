package com.etlas.dto;

import com.etlas.enums.currencyUnits;
import com.etlas.enums.TicketType;
import com.etlas.enums.TripType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDto {
    private TripType tripType;
    private boolean oneWayTrip = true;
    private boolean roundTrip;
    private TicketType ticketType;
    private boolean singleTicket = true;
    private boolean multipleTicket;
    private String pnrNo;
    private String airLine;
    private int ticketAmount;
    private String fromWhere;
    private String ToWhere;
    private String dateRangeString;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime departureTime;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime returnTime;
    private List<CustomerDto> passengers;
    private UserDto boughtUser;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate DateOfPerches;
    private BigDecimal perchesPrice;
    private BigDecimal salesPrice;
    private BigDecimal payedAmount;
    private BigDecimal profit;
    private currencyUnits currencyUnit;
    private UserDto receivedUser;
    private CustomerDto payedCustomer;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfPayed;
    private String notes;

}
