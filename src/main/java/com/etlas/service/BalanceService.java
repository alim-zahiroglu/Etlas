package com.etlas.service;

import com.etlas.dto.BalanceRecordDto;
import com.etlas.entity.Customer;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface BalanceService {
    BalanceRecordDto initiateNewBalance();

    BalanceRecordDto saveBalanceRecord(BalanceRecordDto newRecord);

    List<BalanceRecordDto> getAllBalanceRecords();

    void deleteBalanceRecord(long parseLong);

    BalanceRecordDto getBalanceRecordById(long parseLong);

    BalanceRecordDto initiateUpdateRecord(long parseLong);

    void saveUpdatedBalanceRecord(BalanceRecordDto updatedBalanceRecord);

    BindingResult validateBalanceRecord(BalanceRecordDto newRecord, BindingResult bindingResult);

    boolean isUserReceivedMoney(String userName);

    boolean isCustomerHasBalanceRecord(Customer customer);

    boolean isCardUsedInAnyRecord(String cardId);
}
