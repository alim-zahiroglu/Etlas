package com.etlas.service.impl;

import com.etlas.dto.CardDto;
import com.etlas.entity.Bank;
import com.etlas.entity.Card;
import com.etlas.mapper.MapperUtil;
import com.etlas.repository.BankRepository;
import com.etlas.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {
    private final BankRepository bankRepository;
    private final MapperUtil mapper;
    @Override
    public List<String> getAllBankNames() {
        return bankRepository.findAllByOrderByBankNameAsc();
    }

}
