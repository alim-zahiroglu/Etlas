package com.etlas.converter;

import com.etlas.dto.BalanceRecordDto;
import com.etlas.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BalanceRecordConverter implements Converter<String, BalanceRecordDto> {
    private final BalanceService balanceService;
    @Override
    public BalanceRecordDto convert(String BalanceRecordId) {
        if (BalanceRecordId.equals("") || BalanceRecordId.equals("0")) {
            return null;
        }
        long id = Long.parseLong(BalanceRecordId);
        return balanceService.getBalanceRecordById(id);
    }
}
