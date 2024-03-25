package com.etlas.enums;

import lombok.Getter;

@Getter
public enum PaidType {
    BYHAND("By Hand"), BYCARD("By Card");
    private final String description;

    PaidType(String description) {
        this.description = description;
    }
}
