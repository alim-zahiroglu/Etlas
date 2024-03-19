package com.etlas.entity;

import com.etlas.enums.CountriesTr;
import com.etlas.enums.CustomerType;
import com.etlas.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
//@Where(clause = "is_deleted = false")
public class Customer extends BaseEntity{

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String companyName;

    private String email;

//    @Column(unique = true)
    private String phoneNumber;

//    @Column(unique = true)
    private String officeNumber;

    @Enumerated(EnumType.STRING)
    private CountriesTr country;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private CustomerType customerType;

    @Transient // ignore to save
    private boolean Company;
    @Transient // // ignore to save
    private boolean Individual;

    private BigDecimal customerTRYBalance;
    private BigDecimal customerEURBalance;
    private BigDecimal customerUSDBalance;
}
