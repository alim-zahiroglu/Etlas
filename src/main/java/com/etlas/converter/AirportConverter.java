package com.etlas.converter;

import com.etlas.dto.AirportDto;
import com.etlas.service.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AirportConverter implements Converter<String,AirportDto> {
    private final AirportService airportService;
    @Override
    public AirportDto convert(String id) {
        if (id.equals("")) {
            return null;
        }
        long airportId = Long.parseLong(id);
        return airportService.findById(airportId);
    }
}
