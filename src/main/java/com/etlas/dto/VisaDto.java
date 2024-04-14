package com.etlas.dto;

import com.etlas.enums.CountriesTr;
import com.etlas.enums.CurrencyUnits;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfPerches;
    private BigDecimal perchesPrice;
    private BigDecimal salesPrice;
    private CurrencyUnits currencyUnit;
    private BigDecimal profit;
    private CustomerDto paidCustomer;
    private String note;
    private String uploadedFile;
}
