package com.etlas.service.impl;

import com.etlas.dto.AirLineDto;
import com.etlas.dto.AirportDto;
import com.etlas.entity.Airport;
import com.etlas.mapper.MapperUtil;
import com.etlas.repository.AirportRepository;
import com.etlas.service.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {
    private final AirportRepository repository;
    private final MapperUtil mapper;

    @Override
    public AirportDto findById(Long id) {
        return mapper.convert(repository.findById(id),new AirportDto());
    }

    @Override
    public AirportDto findByIataCode(String iataCode) {
        return mapper.convert(repository.findByIataCode(iataCode),new AirportDto());
    }

    @Override
    public List<AirportDto> getAllAirports() {
       return repository.findAll().stream()
               .map(airport -> mapper.convert(airport, new AirportDto()))
               .collect(Collectors.toList());
    }
}
