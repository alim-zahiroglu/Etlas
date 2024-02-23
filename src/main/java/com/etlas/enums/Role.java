package com.etlas.enums;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("Admin"), MANAGER("Manager"), EMPLOYEE("Employee");
    private final String description;
    Role(String description) {
        this.description = description;
    }
}
