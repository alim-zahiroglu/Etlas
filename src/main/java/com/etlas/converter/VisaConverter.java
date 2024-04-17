package com.etlas.converter;

import com.etlas.dto.VisaDto;
import com.etlas.service.VisaService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class VisaConverter implements Converter<String, VisaDto> {
    private final VisaService visaService;

    @Override
    public VisaDto convert(String visaId) {
        if (visaId.equals("") || visaId.equals("0")) {
            return null;
        }
        long id = Long.parseLong(visaId);
        return visaService.findById(id);
    }
}
