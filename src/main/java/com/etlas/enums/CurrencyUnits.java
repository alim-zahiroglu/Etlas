package com.etlas.enums;

import lombok.Getter;

@Getter
public enum CurrencyUnits {
    TRY("₺ TRY","₺"),
    USD("$ USD", "$"),EUR("€ EUR", "€");
    private final String description;
    private final String currencySymbol;

    CurrencyUnits(String description, String currencySymbol) {
        this.description = description;
        this.currencySymbol = currencySymbol;
    }
}
