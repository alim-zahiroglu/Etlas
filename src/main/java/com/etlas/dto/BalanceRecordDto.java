package com.etlas.dto;

import com.etlas.enums.CurrencyUnits;
import com.etlas.enums.PaidType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BalanceRecordDto {

    @NotNull(message = "please select a receiver")
    private UserDto receiver;

    @NotNull(message = "please select a giver")
    private CustomerDto giver;

    @Min(value = 0, message = "amount must be greater than 0")
    private double amount;
    private CurrencyUnits currencyUnit;

    @NotNull(message = "please select a receiver card")
    private CardDto receiverCard;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    private PaidType paidType;
    private String description;

    private boolean byHand;
    private boolean byCard;


}
