package com.etlas.service;

import com.etlas.dto.VisaDto;
import com.etlas.entity.Customer;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.List;

public interface VisaService {

    VisaDto initializeVisa();

    VisaDto adjustNewVisa(VisaDto newVisa, String addedCustomerId);

    VisaDto saveNewVisa(VisaDto newVisa);

    List<VisaDto> getAllVisas();

    VisaDto findById(long visa);

    VisaDto prepareVisaToUpdate(VisaDto visa);

    void saveUpdatedVisa(VisaDto visaToBeUpdate);

    void deleteVisa(long visaId);

    List<String> getAllUniqueVisTypeWithCountry();

    List<String> getAllUniqueVisTypeWithCountryFromCustomer(long customerId);

    BindingResult validateNewVisa(VisaDto newVisa, BindingResult bindingResult);

    boolean isUserBoughtTicket(String userName);

    boolean isCustomerHasVisa(Customer customer);

    boolean isCardUsedInAnyVisa(String cardId);

    BigDecimal getVisaTRYTotalPerches(String time);

    BigDecimal getVisaUSDTotalPerches(String time);

    BigDecimal getVisaEURTotalPerches(String time);

    BigDecimal getVisaTRYTotalSales(String time);

    BigDecimal getVisaUSDTotalSales(String time);

    BigDecimal getVisaEURTotalSales(String time);

    BigDecimal getVisaTRYTotalProfit(String time);

    BigDecimal getVisaUSDTotalProfit(String time);

    BigDecimal getVisaEURTotalProfit(String time);

    int getTotalTRYPerchesVisa(String time);

    int getTotalUSDPerchesVisa(String time);

    int getTotalEURPerchesVisa(String time);
}
