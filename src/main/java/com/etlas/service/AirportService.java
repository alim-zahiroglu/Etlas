package com.etlas.service;

import com.etlas.dto.AirportDto;

import java.util.List;

public interface AirportService {
    List<AirportDto> getAllAirports();

    AirportDto findByIataCode(String iataCode);

    AirportDto findById(Long id);
}
