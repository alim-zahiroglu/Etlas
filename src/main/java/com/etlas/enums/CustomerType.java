package com.etlas.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomerType {
    COMPANY ("Company"),INDIVIDUAL ("Individual");
    private final String description;
}
