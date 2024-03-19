package com.etlas.entity;

import com.etlas.dto.CustomerDto;
import com.etlas.dto.UserDto;
import com.etlas.enums.CountriesTr;
import com.etlas.enums.CurrencyUnits;
import com.etlas.enums.TicketType;
import com.etlas.enums.TripType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tickets")
public class Ticket extends BaseEntity{

    private TripType tripType;

    @Transient
    private boolean oneWayTrip = true;
    @Transient
    private boolean roundTrip;

    private TicketType ticketType;

    @Transient
    private boolean singleTicket;
    @Transient
    private boolean multipleTicket;

    @Column(unique = true)
    private String pnrNo;

    @ManyToOne
    private AirLine airLine;
    private int ticketAmount;
    @Enumerated(EnumType.STRING)
    private CountriesTr fromWhere; // change to airPortCode enum or object
    @Enumerated(EnumType.STRING)
    private CountriesTr ToWhere;  // change to airPortCode enum or object

    @Column(columnDefinition = "DATE")
    private LocalDateTime departureTime;
    @Column(columnDefinition = "DATE")
    private LocalDateTime returnTime;

    @ManyToMany
    private List<Customer> passengers;

    @ManyToOne
    private User boughtUser;

    @Column(columnDefinition = "DATE")
    private LocalDate DateOfPerches;

    private BigDecimal perchesPrice;
    private BigDecimal salesPrice;
    private BigDecimal payedAmount;
    private BigDecimal profit;

    @Enumerated(EnumType.STRING)
    private CurrencyUnits currencyUnit;

    @ManyToOne
    private User receivedUser;

    @ManyToOne
    private Customer payedCustomer;
    @Column(columnDefinition = "DATE")
    private LocalDate dateOfPayed;
    private String notes;
    private String pdfTicket;
}
