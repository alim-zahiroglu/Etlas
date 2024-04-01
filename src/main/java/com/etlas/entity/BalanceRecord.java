package com.etlas.entity;

import com.etlas.enums.CurrencyUnits;
import com.etlas.enums.PaidType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "balance_records")
public class BalanceRecord extends BaseEntity{

    @ManyToOne
    private Customer giver;

    @ManyToOne
    private User receiver;

    @ManyToOne
    private Card receiverCard;

    private double amount;

    @Enumerated(value = EnumType.STRING)
    private CurrencyUnits currencyUnit;

    @Column(columnDefinition = "DATE")
    private LocalDate date;

    @Enumerated(value = EnumType.STRING)
    private PaidType paidType;
    private String description;

}
