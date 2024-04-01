package com.etlas.service.impl;

import com.etlas.dto.BalanceRecordDto;
import com.etlas.dto.CustomerDto;
import com.etlas.entity.BalanceRecord;
import com.etlas.entity.Customer;
import com.etlas.enums.CurrencyUnits;
import com.etlas.enums.PaidType;
import com.etlas.mapper.MapperUtil;
import com.etlas.repository.BalanceRecordRepository;
import com.etlas.service.BalanceService;
import com.etlas.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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
    public List<BalanceRecordDto> getAllBalanceRecords() {
        List<BalanceRecord> records = repository.findAllByIsDeleted(false);
        return records.stream().map(record -> mapper.convert(record, new BalanceRecordDto())).toList();
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
        } else if (newRecord.isByCard()) {
            newRecord.setPaidType(PaidType.BYCARD);
        }
        calculateCustomerBalance(newRecord);
    }
    private void calculateCustomerBalance(BalanceRecordDto newRecord) {
       if(newRecord.getCurrencyUnit().equals(CurrencyUnits.TRY)){
           newRecord.getGiver().setCustomerTRYBalance(newRecord.getGiver().getCustomerTRYBalance().add(BigDecimal.valueOf(newRecord.getAmount())));
       }
        if(newRecord.getCurrencyUnit().equals(CurrencyUnits.USD)){
            newRecord.getGiver().setCustomerUSDBalance(newRecord.getGiver().getCustomerUSDBalance().add(BigDecimal.valueOf(newRecord.getAmount())));
        }
        if(newRecord.getCurrencyUnit().equals(CurrencyUnits.EUR)){
            newRecord.getGiver().setCustomerEURBalance(newRecord.getGiver().getCustomerEURBalance().add(BigDecimal.valueOf(newRecord.getAmount())));
        }
        customerService.save(newRecord.getGiver());
    }

    @Override
    public void saveBalanceRecordFromTicket(BalanceRecordDto balanceRecord) {
        repository.save(mapper.convert(balanceRecord, new BalanceRecord()));
    }

    @Override
    public void deleteBalanceRecord(long recordId) {
        BalanceRecord record = repository.findById(recordId).orElseThrow(NoSuchFieldError::new);
        Customer giver = record.getGiver();

        // undo the balance
        if (record.getCurrencyUnit().equals(CurrencyUnits.TRY)) {
            giver.setCustomerTRYBalance(giver.getCustomerTRYBalance().subtract(BigDecimal.valueOf(record.getAmount())));
        }
        if (record.getCurrencyUnit().equals(CurrencyUnits.USD)) {
            giver.setCustomerUSDBalance(giver.getCustomerUSDBalance().subtract(BigDecimal.valueOf(record.getAmount())));
        }
        if (record.getCurrencyUnit().equals(CurrencyUnits.EUR)) {
            giver.setCustomerEURBalance(giver.getCustomerEURBalance().subtract(BigDecimal.valueOf(record.getAmount())));
        }
        customerService.save(mapper.convert(giver, new CustomerDto()));
        record.setDeleted(true);
        repository.save(record);
    }

    @Override
    public void deleteBalanceRecordFromTicket(long recordId) {
        BalanceRecord record = repository.findById(recordId).orElseThrow(NoSuchFieldError::new);
        record.setDeleted(true);
        repository.save(record);
    }

    @Override
    public long findRecordIdByLinkedTicketId(long linkedTickedId) {
        return repository.findByLinkedTicketIdAndIsDeleted(linkedTickedId, false).getId();
    }
}
