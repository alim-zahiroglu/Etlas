package com.etlas.service;

import com.etlas.dto.BalanceRecordDto;

public interface BalanceService {
    BalanceRecordDto initiateNewBalance();

    BalanceRecordDto saveBalanceRecord(BalanceRecordDto newRecord);
}
