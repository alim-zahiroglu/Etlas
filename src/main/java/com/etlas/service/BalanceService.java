package com.etlas.service;

import com.etlas.dto.BalanceRecordDto;

import java.util.List;

public interface BalanceService {
    BalanceRecordDto initiateNewBalance();

    BalanceRecordDto saveBalanceRecord(BalanceRecordDto newRecord);

    List<BalanceRecordDto> getAllBalanceRecords();

    void deleteBalanceRecord(long parseLong);
}
