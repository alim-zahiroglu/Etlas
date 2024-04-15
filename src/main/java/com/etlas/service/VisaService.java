package com.etlas.service;

import com.etlas.dto.VisaDto;

public interface VisaService {

    VisaDto initializeVisa();

    VisaDto adjustNewVisa(VisaDto newVisa, String addedCustomerId);

    VisaDto saveNewVisa(VisaDto newVisa);
}
