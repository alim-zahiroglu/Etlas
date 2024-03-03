package com.etlas.entity;

import com.etlas.enums.CustomerType;
import com.etlas.enums.Gender;
import com.etlas.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

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

    private String companyName;

    private String email;

//    @Column(unique = true)
    private String phone;

//    @Column(unique = true)
    private String OfficePhone;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private CustomerType customerType;

    @Transient // ignore to save
    private boolean Company;
    @Transient // // ignore to save
    private boolean Individual = true;

    private BigDecimal customerTRYBalance;
    private BigDecimal customerEURBalance;
    private BigDecimal customerUSDBalance;
}
