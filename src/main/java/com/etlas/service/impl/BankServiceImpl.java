package com.etlas.service.impl;

import com.etlas.entity.Bank;
import com.etlas.mapper.MapperUtil;
import com.etlas.repository.BankRepository;
import com.etlas.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {
    private final BankRepository repository;
    private final MapperUtil mapper;
    @Override
    public List<String> getAllBankNames() {
        return repository.findAllByOrderByBankNameAsc();
    }

    @Override
    public boolean isBankNameExist(String bankName) {
        return repository.existsByBankName(bankName);
    }

    @Override
    public void saveBankName(Bank newBank) {
        repository.save(newBank);
    }
}
