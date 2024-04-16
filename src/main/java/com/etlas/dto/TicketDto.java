package com.etlas.dto;

import com.etlas.enums.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "Please enter PNR")
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

    @Min(value = 0, message = "Please enter a valid price")
    private BigDecimal perchesPrice;

    @Min(value = 0, message = "Please enter a valid price")
    private BigDecimal salesPrice;

    @NotNull(message = "Please select a paid card")
    private CardDto paidCard;

    private BigDecimal profit;
    private CurrencyUnits currencyUnit;

    private CustomerDto payedCustomer;

    private String notes;
    private String pdfTicket;

    @NotEmpty(message = "Please select at least one passenger")
    private List<String> passengersUI;

    @NotEmpty(message = "Please select paid customer")
    private String payedCustomerUI;

}
