package com.etlas.dto;

import com.etlas.enums.CountriesTr;
import com.etlas.enums.CurrencyUnits;
import com.etlas.enums.TicketType;
import com.etlas.enums.TripType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDto {
    private long id;
    private TripType tripType;
    private boolean oneWayTrip;
    private boolean roundTrip;
    private TicketType ticketType;
    private boolean singleTicket;
    private boolean multipleTicket;

    @NotBlank(message = "PNR shouldn't be blank")
    private String pnrNo;
    private AirLineDto airLine;
    private int ticketAmount;
    private AirportDto fromWhere;
    private AirportDto toWhere;
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
    private CurrencyUnits currencyUnit;
    private UserDto receivedUser;
    private CustomerDto payedCustomer;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfPayed;
    private String notes;
    private String pdfTicket;

    private String payedCustomerUI;

    @NotBlank(message = "Passengers shouldn't be blank")
    private List<String> passengersUI;

}
