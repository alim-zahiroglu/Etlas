package com.etlas.dto;

import com.etlas.enums.CustomerType;
import com.etlas.enums.Gender;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CustomerDto {
    private long id;

    @NotBlank(message = "first name should not be blank")
    @Size(min = 2, max = 50, message = "first name must be 2~50 character long")
    private String firstName;

    @NotBlank(message = "last name should not be blank")
    @Size(min = 2, max = 50, message = "last name must be 2~50 character long")
    private String lastName;

    @NotBlank(message = "Company name should not be blank")
    @Size(min = 2, max = 50, message = "Company name must be 2~50 character long")
    private String companyName;

    @Email(message = "please enter a valid email")
    private String email;

    @NotBlank(message = "Please enter the phone number\n ex: 05552223344")
    private String phone;
    private String OfficePhone;

    private Gender gender;

    private CustomerType customerType;
    private boolean Company;
    private boolean Individual = true;

    private BigDecimal customerTRYBalance;
    private BigDecimal customerEURBalance;
    private BigDecimal customerUSDBalance;
}
