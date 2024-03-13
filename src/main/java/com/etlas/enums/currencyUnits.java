package com.etlas.enums;

import lombok.Getter;

@Getter
public enum currencyUnits {
    TRY("₺ TRY","₺"),
    USD("$ USD", "$"),EUR("€ EUR", "€");
    private final String description;
    private final String currencySymbol;

    currencyUnits(String description, String currencySymbol) {
        this.description = description;
        this.currencySymbol = currencySymbol;
    }
}
