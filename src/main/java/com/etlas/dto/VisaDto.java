package com.etlas.dto;

import com.etlas.enums.CountriesTr;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisaDto {
    long id;
    private CountriesTr country;
    private String visaType;
    private CustomerDto customer;
    private UserDto boughtUser;
    private CardDto paidCard;
    private LocalDate dateOfPerches;
    private BigDecimal perchesPrice;
    private BigDecimal salesPrice;
    private BigDecimal profit;
    private CustomerDto paidCustomer;
    private String note;
    private String uploadedFile;
}
