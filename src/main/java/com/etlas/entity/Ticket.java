package com.etlas.entity;

import com.etlas.enums.CurrencyUnits;
import com.etlas.enums.TicketType;
import com.etlas.enums.TripType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Enumerated(EnumType.STRING)
    private TripType tripType;

    @Transient
    private boolean oneWayTrip;
    @Transient
    private boolean roundTrip;

    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    @Transient
    private boolean singleTicket;
    @Transient
    private boolean multipleTicket;

    @Column(unique = true)
    private String pnrNo;

    private String ticketNo;

    @ManyToOne
    private AirLine airLine;
    private int ticketAmount;

    @ManyToOne
    private Airport fromWhere;
    @ManyToOne
    private Airport ToWhere;

    @Column(columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime departureTime;

    @Column(columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime returnTime;

    @ManyToMany(cascade = CascadeType.REMOVE)
    private List<Customer> passengers;

    @ManyToOne
    private User boughtUser;

    @Column(columnDefinition = "DATE")
    private LocalDate dateOfPerches;

    private BigDecimal perchesPrice;
    private BigDecimal salesPrice;
    private BigDecimal profit;

    @Enumerated(EnumType.STRING)
    private CurrencyUnits currencyUnit;

    @ManyToOne
    private Card paidCard;

    @ManyToOne
    private Customer payedCustomer;

    private String notes;
    private String pdfTicket;
}
