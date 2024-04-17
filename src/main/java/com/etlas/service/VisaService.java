package com.etlas.service;

import com.etlas.dto.VisaDto;

import java.util.List;

public interface VisaService {

    VisaDto initializeVisa();

    VisaDto adjustNewVisa(VisaDto newVisa, String addedCustomerId);

    VisaDto saveNewVisa(VisaDto newVisa);

    List<VisaDto> getAllVisas();

    VisaDto findById(long visa);

    VisaDto prepareVisaToUpdate(VisaDto visa);

    void saveUpdatedVisa(VisaDto visaToBeUpdate);

    void deleteVisa(long visaId);

    List<String> getAllUniqueVisTypeWithCountry();
}
