package com.etlas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cards")
public class Card extends BaseEntity{

    private String cardOwner;

    private String bankName;

    private BigDecimal availableLimitTRY;

    private BigDecimal availableLimitUSD;

    private BigDecimal availableLimitEUR;

    private String dueDate;
}
