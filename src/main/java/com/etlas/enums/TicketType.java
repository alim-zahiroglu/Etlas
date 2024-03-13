package com.etlas.enums;

import lombok.Getter;

@Getter
public enum TicketType {
    SINGLE("Single ticket"), MULTIPLE("Multiple ticket");
    private final String description;

    TicketType(String description) {
        this.description = description;
    }
}
