package com.etlas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AirportDto {
    private long id;
    private String airport;
    private String city;
    private String country;
    private String iataCode;
}

