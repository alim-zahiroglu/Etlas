package com.etlas.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
public class CardDto {
    private Long id;
    @NotBlank(message = "Card owner name is mandatory")
    private String cardOwner;

    @NotBlank(message = "Bank name is mandatory")
    private String bankName;

//    @Min(value = 0, message = "Available limit must be greater than or equal to 0")
    private BigDecimal availableLimitTRY;

//    @Min(value = 0, message = "Available limit must be greater than or equal to 0")
    private BigDecimal availableLimitUSD;

//    @Min(value = 0, message = "Available limit must be greater than or equal to 0")
    private BigDecimal availableLimitEUR;

    private String dueDate;

    private String availableLimitTRYUI;
    private String availableLimitUSDUI;
    private String availableLimitEURUI;

    private String fromForUpdateUI;

    private String fromForAddBalanceUI;

}
