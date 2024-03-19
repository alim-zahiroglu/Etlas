package com.etlas.service.impl;

import com.etlas.dto.AirLineDto;
import com.etlas.entity.AirLine;
import com.etlas.mapper.MapperUtil;
import com.etlas.repository.AirLineRepository;
import com.etlas.service.AirLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AirLineServiceImpl implements AirLineService {
    private final AirLineRepository repository;
    private final MapperUtil mapper;

    @Override
    public AirLineDto findById(long id) {
        return mapper.convert(repository.findById(id), new AirLineDto());
    }

    @Override
    public AirLineDto findByName(String airLineName) {
        return mapper.convert(repository.findByName(airLineName), new AirLineDto());
    }

    @Override
    public List<AirLineDto> getAllAirLines() {

        List<AirLine> airlines = repository.findAll();

        return airlines.stream()
                .map(airline-> mapper.convert(airline,new AirLineDto()))
                .collect(Collectors.toList());
    }
}
