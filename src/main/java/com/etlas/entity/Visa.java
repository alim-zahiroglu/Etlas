package com.etlas.entity;

import com.etlas.dto.CardDto;
import com.etlas.dto.CustomerDto;
import com.etlas.dto.UserDto;
import com.etlas.enums.CountriesTr;
import com.etlas.enums.CurrencyUnits;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "visas")
public class Visa extends BaseEntity{

    @Enumerated(EnumType.STRING)
    private CountriesTr country;

    private String visaType;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private User boughtUser;

    @ManyToOne
    private Card paidCard;

    @Column(columnDefinition = "DATE")
    private LocalDate dateOfPerches;
    private BigDecimal perchesPrice;
    private BigDecimal salesPrice;

    @Enumerated(EnumType.STRING)
    private CurrencyUnits currencyUnit;
    private BigDecimal profit;

    @ManyToOne
    private Customer paidCustomer;
    private String note;
    private String uploadedFile;
}
