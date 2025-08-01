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

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BalanceRecordDto {
    private Long id;

    @NotNull(message = "please select a receiver")
    private UserDto receiver;

    private CustomerDto giver;

    private BigDecimal amount;
    private CurrencyUnits currencyUnit;

    private CardDto receiverCard;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;

    private PaidType paidType;

    private String note;

    private boolean byHand;
    private boolean byCard;

    private String linkedTicket;
    private String linkedVisa;


}
