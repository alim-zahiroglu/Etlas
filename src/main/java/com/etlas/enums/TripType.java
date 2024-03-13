package com.etlas.enums;

import lombok.Getter;

@Getter
public enum TripType {
    ONEWAY("One way trip"), ROUND("Round trip");
    private final String description;
    TripType(String description) {
        this.description = description;
    }
}
