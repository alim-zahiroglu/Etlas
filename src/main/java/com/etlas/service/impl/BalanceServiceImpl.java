package com.etlas.service.impl;

import com.etlas.dto.BalanceRecordDto;
import com.etlas.dto.CustomerDto;
import com.etlas.dto.UserDto;
import com.etlas.entity.BalanceRecord;
import com.etlas.entity.Customer;
import com.etlas.enums.CurrencyUnits;
import com.etlas.enums.PaidType;
import com.etlas.mapper.MapperUtil;
import com.etlas.repository.BalanceRecordRepository;
import com.etlas.service.BalanceService;
import com.etlas.service.CustomerService;
import com.etlas.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {
    private final BalanceRecordRepository repository;
    private final MapperUtil mapper;
    private final CustomerService customerService;
    private final SecurityService securityService;

    @Override
    public BalanceRecordDto initiateNewBalance() {
        UserDto currentUser = securityService.getLoggedInUser();
        return BalanceRecordDto.builder()
                .currencyUnit(CurrencyUnits.TRY)
                .receiver(currentUser)
                .amount(BigDecimal.ZERO)
                .byHand(true)
                .build();
    }

    @Override
    public BalanceRecordDto initiateUpdateRecord(long recordId) {
        BalanceRecordDto recordDto = getBalanceRecordById(recordId);

        if (recordDto.getPaidType().equals(PaidType.BYHAND)){
            recordDto.setByHand(true);
            recordDto.setByCard(false);
        }
        if (recordDto.getPaidType().equals(PaidType.BYCARD)) {
            recordDto.setByCard(true);
            recordDto.setByHand(false);
        }
        return recordDto;
    }

    @Override
    public BalanceRecordDto getBalanceRecordById(long recordId) {
         BalanceRecord record = repository.findByIdAndIsDeletedFalse(recordId).orElseThrow(NoSuchFieldError::new);
         return mapper.convert(record, new BalanceRecordDto());
    }

    @Override
    public List<BalanceRecordDto> getAllBalanceRecords() {
        List<BalanceRecord> records = repository.findAllByIsDeletedOrderByLastUpdateDateTimeDesc(false);
        return records.stream().map(record -> mapper.convert(record, new BalanceRecordDto())).toList();
    }

    @Override
    public BindingResult validateBalanceRecord(BalanceRecordDto newRecord, BindingResult bindingResult) {
        if (newRecord.getAmount() ==null) newRecord.setAmount(BigDecimal.ZERO);
        if (newRecord.getGiver() == null) {
            bindingResult.rejectValue("giver", "error.giver", "Please select a giver");
        }
        if (newRecord.getReceiver() == null) {
            bindingResult.rejectValue("receiver", "error.receiver", "Please select a receiver");
        }
        if (newRecord.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            bindingResult.rejectValue("amount", "error.amount", "Amount must be greater than zero");
        }
        if (newRecord.isByCard() && newRecord.getReceiverCard() == null) {
            bindingResult.rejectValue("receiverCard", "error.receiverCard", "Please select a card");
        }
        return bindingResult;
    }

    @Override
    public BalanceRecordDto saveBalanceRecord(BalanceRecordDto newRecord) {
        prepareRecordToSave(newRecord);
        calculateCustomerBalance(newRecord);
        BalanceRecord savedRecord = repository.save(mapper.convert(newRecord, new BalanceRecord()));
        return mapper.convert(savedRecord, new BalanceRecordDto());
    }
    private void prepareRecordToSave(BalanceRecordDto newRecord) {
        if (Objects.equals(newRecord.getLinkedTicket(), "0")) {
            newRecord.setLinkedTicket("");
        }
        if (Objects.equals(newRecord.getLinkedVisa(), "0")) {
            newRecord.setLinkedVisa("");
        }
        if (newRecord.isByHand()) {
            newRecord.setPaidType(PaidType.BYHAND);
        }
        if (newRecord.isByCard()) {
            newRecord.setPaidType(PaidType.BYCARD);
        }
    }
    private void calculateCustomerBalance(BalanceRecordDto newRecord) {
        CustomerDto giver = customerService.findById(newRecord.getGiver().getId());
       if(newRecord.getCurrencyUnit().equals(CurrencyUnits.TRY)){
           giver.setCustomerTRYBalance(giver.getCustomerTRYBalance().add(newRecord.getAmount()));
       }
        if(newRecord.getCurrencyUnit().equals(CurrencyUnits.USD)){
            giver.setCustomerUSDBalance(giver.getCustomerUSDBalance().add(newRecord.getAmount()));
        }
        if(newRecord.getCurrencyUnit().equals(CurrencyUnits.EUR)){
            giver.setCustomerEURBalance(giver.getCustomerEURBalance().add(newRecord.getAmount()));
        }
        customerService.save(giver);
    }

    @Override
    public void saveUpdatedBalanceRecord(BalanceRecordDto updatedBalanceRecord) {
        BalanceRecord oldRecord = repository.findById(updatedBalanceRecord.getId()).orElseThrow(NoSuchFieldError::new);
        resetCustomerBalance(oldRecord); // reset the customer balance
        saveBalanceRecord(updatedBalanceRecord);  // save the updated record

    }

    @Override
    public void deleteBalanceRecord(long recordId) {
        BalanceRecord record = repository.findById(recordId).orElseThrow(NoSuchFieldError::new);
        record.setDeleted(true);
        repository.save(record);
    }

    private void resetCustomerBalance(BalanceRecord record) {
        Customer giver = record.getGiver();
        if (record.getCurrencyUnit().equals(CurrencyUnits.TRY)) {
            giver.setCustomerTRYBalance(giver.getCustomerTRYBalance().subtract(record.getAmount()));
        }
        if (record.getCurrencyUnit().equals(CurrencyUnits.USD)) {
            giver.setCustomerUSDBalance(giver.getCustomerUSDBalance().subtract(record.getAmount()));
        }
        if (record.getCurrencyUnit().equals(CurrencyUnits.EUR)) {
            giver.setCustomerEURBalance(giver.getCustomerEURBalance().subtract(record.getAmount()));
        }
        customerService.save(mapper.convert(giver, new CustomerDto()));
    }

    @Override
    public boolean isUserReceivedMoney(String userName) {
        return repository.existsByReceiver_UserNameAndIsDeleted(userName,false);
    }

    @Override
    public boolean isCustomerHasBalanceRecord(Customer customer) {
        return repository.existsByGiverAndIsDeleted(customer,false);
    }
}
