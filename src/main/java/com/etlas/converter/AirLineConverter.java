package com.etlas.converter;

import com.etlas.dto.AirLineDto;
import com.etlas.dto.UserDto;
import com.etlas.service.AirLineService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AirLineConverter implements Converter<String, AirLineDto> {
    private final AirLineService airLineService;

    public AirLineConverter(AirLineService airLineService) {
        this.airLineService = airLineService;
    }

    @Override
    public AirLineDto convert(String airLineId) {
        if (airLineId.equals("")) {
            return null;
        }
        long id = Long.parseLong(airLineId);
        return airLineService.findById(id);
    }

}
