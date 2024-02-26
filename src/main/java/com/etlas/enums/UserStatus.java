package com.etlas.enums;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserStatus {
    TRUE(true,"Active"),FALSE(false,"Inactive");
    private final Boolean value;
    private final String description;
}
