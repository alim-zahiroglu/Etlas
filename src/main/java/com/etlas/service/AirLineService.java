package com.etlas.service;

import com.etlas.dto.AirLineDto;

import java.util.List;

public interface AirLineService {

    List<AirLineDto> getAllAirLines();

    AirLineDto findById(long id);

    AirLineDto findByName(String airlineName);
}
