package com.etlas.service;


import com.etlas.entity.Bank;

import java.util.List;

public interface BankService {
    List<String> getAllBankNames();

    boolean isBankNameExist(String bankName);

    void saveBankName(Bank build);
}
