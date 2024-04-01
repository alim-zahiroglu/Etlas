package com.etlas.service.impl;

import com.etlas.dto.BalanceRecordDto;
import com.etlas.dto.CustomerDto;
import com.etlas.entity.BalanceRecord;
import com.etlas.enums.CurrencyUnits;
import com.etlas.enums.PaidType;
import com.etlas.mapper.MapperUtil;
import com.etlas.repository.BalanceRecordRepository;
import com.etlas.service.BalanceService;
import com.etlas.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {
    private final BalanceRecordRepository repository;
    private final MapperUtil mapper;
    private final CustomerService customerService;
    @Override
    public BalanceRecordDto initiateNewBalance() {
        return BalanceRecordDto.builder()
                .currencyUnit(CurrencyUnits.TRY)
                .byHand(true)
                .build();
    }

    @Override
    public BalanceRecordDto saveBalanceRecord(BalanceRecordDto newRecord) {
        prepareRecordToSave(newRecord);
        BalanceRecord savedRecord = repository.save(mapper.convert(newRecord, new BalanceRecord()));
        return mapper.convert(savedRecord, new BalanceRecordDto());
    }
    private void prepareRecordToSave(BalanceRecordDto newRecord) {
        if (newRecord.isByHand()) {
            newRecord.setPaidType(PaidType.BYHAND);
        }
        newRecord.setPaidType(PaidType.BYCARD);
        calculateCustomerBalance(newRecord);
    }
    private void calculateCustomerBalance(BalanceRecordDto newRecord) {
       if(newRecord.getCurrencyUnit().equals(CurrencyUnits.TRY)){
           newRecord.getGiver().setCustomerTRYBalance(newRecord.getGiver().getCustomerTRYBalance().subtract(BigDecimal.valueOf(newRecord.getAmount())));
       }
        if(newRecord.getCurrencyUnit().equals(CurrencyUnits.USD)){
            newRecord.getGiver().setCustomerUSDBalance(newRecord.getGiver().getCustomerUSDBalance().subtract(BigDecimal.valueOf(newRecord.getAmount())));
        }
        if(newRecord.getCurrencyUnit().equals(CurrencyUnits.EUR)){
            newRecord.getGiver().setCustomerEURBalance(newRecord.getGiver().getCustomerEURBalance().subtract(BigDecimal.valueOf(newRecord.getAmount())));
        }
        customerService.save(newRecord.getGiver());
    }

}
