package com.etlas.dto;

import com.etlas.enums.CountriesTr;
import com.etlas.enums.CurrencyUnits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

    @NotNull(message = "Please select a paid card")
    private CardDto paidCard;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfPerches;

    @Min(value = 0, message = "Please enter a valid price")
    private BigDecimal perchesPrice;

    @Min(value = 0, message = "Please enter a valid price")
    private BigDecimal salesPrice;
    private CurrencyUnits currencyUnit;
    private BigDecimal profit;

    private CustomerDto paidCustomer;

    private String note;
    private String uploadedFile;

    @NotEmpty(message = "Please select customer")
    private String customerUI;

    @NotEmpty(message = "Please select paid customer")
    private String paidCustomerUI;
}
