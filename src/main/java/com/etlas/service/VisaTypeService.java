package com.etlas.service;

import com.etlas.dto.VisaDto;
import com.etlas.entity.VisaType;

import java.util.List;

public interface VisaTypeService {
    List<String> getAllVisaTypes();

    VisaType findByName(String visaType);

    void save(VisaType newType);
}
