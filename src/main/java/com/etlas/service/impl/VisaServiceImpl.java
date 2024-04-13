package com.etlas.service.impl;

import com.etlas.dto.VisaDto;
import com.etlas.enums.CountriesTr;
import com.etlas.enums.CurrencyUnits;
import com.etlas.service.VisaService;
import org.springframework.stereotype.Service;

@Service
public class VisaServiceImpl implements VisaService {

    @Override
    public VisaDto initializeVisa() {
        return VisaDto.builder()
                .visaType("ömre vizasi")
                .currencyUnit(CurrencyUnits.USD)
                .country(CountriesTr.SAU)
                .build();
    }
}
