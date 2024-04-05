package com.etlas.service;

import com.etlas.dto.BalanceRecordDto;

import java.util.List;

public interface BalanceService {
    BalanceRecordDto initiateNewBalance();

    BalanceRecordDto saveBalanceRecord(BalanceRecordDto newRecord);

    List<BalanceRecordDto> getAllBalanceRecords();

    void deleteBalanceRecord(long parseLong);

    void saveBalanceRecordFromTicket(BalanceRecordDto balanceRecord);

    long findRecordIdByLinkedTicketId(long linkedTicketId);

    BalanceRecordDto getBalanceRecordById(long parseLong);

    BalanceRecordDto initiateUpdateRecord(long parseLong);

    void saveUpdatedBalanceRecord(BalanceRecordDto updatedBalanceRecord);

    void removeOldBalance(long recordId);
}
