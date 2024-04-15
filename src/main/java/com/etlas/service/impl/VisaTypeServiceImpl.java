package com.etlas.service.impl;

import com.etlas.entity.VisaType;
import com.etlas.repository.VisaTypeRepository;
import com.etlas.service.VisaTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VisaTypeServiceImpl implements VisaTypeService {
    private final VisaTypeRepository visaTypeRepository;
    @Override
    public List<String> getAllVisaTypes() {
       return visaTypeRepository.findAllVisaTypes();
    }
}
