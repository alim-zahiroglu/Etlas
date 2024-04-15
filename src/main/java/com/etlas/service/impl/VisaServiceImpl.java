package com.etlas.service.impl;

import com.etlas.dto.CustomerDto;
import com.etlas.dto.VisaDto;
import com.etlas.entity.Visa;
import com.etlas.enums.CountriesTr;
import com.etlas.enums.CurrencyUnits;
import com.etlas.mapper.MapperUtil;
import com.etlas.repository.VisaRepository;
import com.etlas.service.CustomerService;
import com.etlas.service.VisaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisaServiceImpl implements VisaService {
    private final CustomerService customerService;
    private final VisaRepository repository;
    private final MapperUtil mapper;

    @Override
    public VisaDto initializeVisa() {
        return VisaDto.builder()
                .visaType("ömre vizasi")
                .currencyUnit(CurrencyUnits.USD)
                .country(CountriesTr.SAU)
                .perchesPrice(BigDecimal.ZERO)
                .salesPrice(BigDecimal.ZERO)
                .build();
    }

    @Override
    public List<VisaDto> getAllVisas() {
        return  repository.findAllByIsDeletedFalse().stream()
                .map(visa -> mapper.convert(visa, new VisaDto()))
                .toList();
    }

    @Override
    public VisaDto adjustNewVisa(VisaDto newVisa, String addedCustomerId) {
        CustomerDto customer = customerService.getCustomerById(Long.parseLong(addedCustomerId));
       newVisa.setCustomer(customer);
       newVisa.setPaidCustomer(customer);
       return newVisa;
    }

    @Override
    public VisaDto saveNewVisa(VisaDto newVisa) {
        Visa savedVisa = repository.save(mapper.convert(newVisa, new Visa()));
        return mapper.convert(savedVisa, new VisaDto());
    }
}
