package com.etlas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardBalanceDto {
    @NotNull(message = "Card can not be empty, Select a card")
    private CardDto card;
    private BigDecimal tryBalance;
    private BigDecimal usdBalance;
    private BigDecimal eurBalance;
}
