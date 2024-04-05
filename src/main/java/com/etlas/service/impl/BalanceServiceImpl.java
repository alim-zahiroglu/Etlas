package com.etlas.service.impl;

import com.etlas.dto.BalanceRecordDto;
import com.etlas.dto.CustomerDto;
import com.etlas.dto.TicketDto;
import com.etlas.entity.BalanceRecord;
import com.etlas.entity.Customer;
import com.etlas.enums.CurrencyUnits;
import com.etlas.enums.PaidType;
import com.etlas.mapper.MapperUtil;
import com.etlas.repository.BalanceRecordRepository;
import com.etlas.service.BalanceService;
import com.etlas.service.CustomerService;
import com.etlas.service.TicketService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BalanceServiceImpl implements BalanceService {
    private final BalanceRecordRepository repository;
    private final MapperUtil mapper;
    private final CustomerService customerService;
    private final TicketService ticketService;

    public BalanceServiceImpl(BalanceRecordRepository repository, MapperUtil mapper, CustomerService customerService, @Lazy TicketService ticketService) {
        this.repository = repository;
        this.mapper = mapper;
        this.customerService = customerService;
        this.ticketService = ticketService;
    }

    @Override
    public BalanceRecordDto initiateNewBalance() {
        return BalanceRecordDto.builder()
                .currencyUnit(CurrencyUnits.TRY)
                .byHand(true)
                .build();
    }

    @Override
    public BalanceRecordDto initiateUpdateRecord(long recordId) {
        BalanceRecordDto recordDto = getBalanceRecordById(recordId);
        if (recordDto.getPaidType().equals(PaidType.BYHAND)){
            recordDto.setByHand(true);
            recordDto.setByCard(false);
        } else if (recordDto.getPaidType().equals(PaidType.BYCARD)) {
            recordDto.setByCard(true);
            recordDto.setByHand(false);
        }
        return recordDto;
    }

    @Override
    public BalanceRecordDto getBalanceRecordById(long recordId) {
         BalanceRecord record = repository.findById(recordId).orElseThrow(NoSuchFieldError::new);
         if (record != null) {
             return mapper.convert(record, new BalanceRecordDto());
         }
            return null;
    }

    @Override
    public List<BalanceRecordDto> getAllBalanceRecords() {
        List<BalanceRecord> records = repository.findAllByIsDeleted(false);
        return records.stream().map(record -> mapper.convert(record, new BalanceRecordDto())).toList();
    }

    @Override
    public BalanceRecordDto saveBalanceRecord(BalanceRecordDto newRecord) {
        prepareRecordToSave(newRecord);
        calculateCustomerBalance(newRecord);
        BalanceRecord savedRecord = repository.save(mapper.convert(newRecord, new BalanceRecord()));
        return mapper.convert(savedRecord, new BalanceRecordDto());
    }
    private void prepareRecordToSave(BalanceRecordDto newRecord) {
        if (newRecord.isByHand()) {
            newRecord.setPaidType(PaidType.BYHAND);
        } else if (newRecord.isByCard()) {
            newRecord.setPaidType(PaidType.BYCARD);
        }
    }
    private void calculateCustomerBalance(BalanceRecordDto newRecord) {
        CustomerDto giver = customerService.findById(newRecord.getGiver().getId());
       if(newRecord.getCurrencyUnit().equals(CurrencyUnits.TRY)){
           giver.setCustomerTRYBalance(newRecord.getGiver().getCustomerTRYBalance().add(BigDecimal.valueOf(newRecord.getAmount())));
       }
        if(newRecord.getCurrencyUnit().equals(CurrencyUnits.USD)){
            giver.setCustomerUSDBalance(newRecord.getGiver().getCustomerUSDBalance().add(BigDecimal.valueOf(newRecord.getAmount())));
        }
        if(newRecord.getCurrencyUnit().equals(CurrencyUnits.EUR)){
            giver.setCustomerEURBalance(newRecord.getGiver().getCustomerEURBalance().add(BigDecimal.valueOf(newRecord.getAmount())));
        }
        customerService.save(giver);
    }

    @Override
    public void saveUpdatedBalanceRecord(BalanceRecordDto updatedBalanceRecord) {
        deleteBalanceRecord(updatedBalanceRecord.getId()); // delete the old record
        saveBalanceRecord(updatedBalanceRecord);         // save the updated record

        TicketDto linkedTicket = ticketService.findById(updatedBalanceRecord.getLinkedTicketId());

        if (linkedTicket != null) {
            linkedTicket.setPayedAmount(BigDecimal.valueOf(updatedBalanceRecord.getAmount()));
            ticketService.save(linkedTicket);
        }

    }

    @Override
    public void saveBalanceRecordFromTicket(BalanceRecordDto balanceRecord) {
        repository.save(mapper.convert(balanceRecord, new BalanceRecord()));
    }

    @Override
    public void deleteBalanceRecord(long recordId) {
        BalanceRecord record = repository.findById(recordId).orElseThrow(NoSuchFieldError::new);
        resetCustomerBalance(record);         // undo the customer balance
        deleteOldTicketPaidAmount(record);    // reset the ticket paid amount
        record.setDeleted(true);
        repository.save(record);
    }

    private void resetCustomerBalance(BalanceRecord record) {
        Customer giver = record.getGiver();
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
    }

    private void deleteOldTicketPaidAmount(BalanceRecord record) {
        TicketDto linkedTicket = ticketService.findById(record.getLinkedTicketId());
        if (linkedTicket != null) {
            linkedTicket.setPayedAmount(BigDecimal.valueOf(record.getAmount()));
            ticketService.save(linkedTicket);
        }
    }

    @Override
    public void removeOldBalance(long recordId) {
        BalanceRecord record = repository.findById(recordId).orElseThrow(NoSuchFieldError::new);
        record.setDeleted(true);
        repository.save(record);
    }

    @Override
    public long findRecordIdByLinkedTicketId(long linkedTickedId) {
        BalanceRecord record = repository.findByLinkedTicketIdAndIsDeleted(linkedTickedId, false);
        if (record != null) {
            return record.getId();
        }
        return 0;
    }
}
