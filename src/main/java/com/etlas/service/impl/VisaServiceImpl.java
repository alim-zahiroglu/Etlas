package com.etlas.service.impl;

import com.etlas.dto.CardDto;
import com.etlas.dto.CustomerDto;
import com.etlas.dto.VisaDto;
import com.etlas.entity.Visa;
import com.etlas.enums.CountriesTr;
import com.etlas.enums.CurrencyUnits;
import com.etlas.mapper.MapperUtil;
import com.etlas.repository.VisaRepository;
import com.etlas.service.CardService;
import com.etlas.service.CustomerService;
import com.etlas.service.VisaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisaServiceImpl implements VisaService {
    private final CustomerService customerService;
    private final CardService cardService;
    private final VisaRepository repository;
    private final MapperUtil mapper;

    @Override
    public VisaDto initializeVisa() {
        return VisaDto.builder()
                .visaType("ömre vizasi")
                .currencyUnit(CurrencyUnits.USD)
                .country(CountriesTr.SAU)
                .perchesPrice(BigDecimal.ZERO)
                .salesPrice(BigDecimal.ZERO)
                .build();
    }

    @Override
    public List<VisaDto> getAllVisas() {
        return  repository.findAllByIsDeletedFalse().stream()
                .map(visa -> mapper.convert(visa, new VisaDto()))
                .toList();
    }

    @Override
    public VisaDto adjustNewVisa(VisaDto newVisa, String addedCustomerId) {
        CustomerDto customer = customerService.getCustomerById(Long.parseLong(addedCustomerId));
       newVisa.setCustomer(customer);
       newVisa.setPaidCustomer(customer);
       return newVisa;
    }

    @Override
    public VisaDto saveNewVisa(VisaDto newVisa) {
        //set customer and paid customer
        setCustomerAndPaidCustomer(newVisa);

        // calculate profit
        calculateProfit(newVisa);

        // calculate customer debt
        calculateCustomerDebt(newVisa);

        // calculate credit card debt
        calculateCreditCardDebt(newVisa);

        // save new visa
        Visa savedVisa = repository.save(mapper.convert(newVisa, new Visa()));
        return mapper.convert(savedVisa, new VisaDto());
    }

    private void setCustomerAndPaidCustomer(VisaDto newVisa) {
        newVisa.setCustomer(customerService.getCustomerById(Long.parseLong(newVisa.getCustomerUI())));
        newVisa.setPaidCustomer(customerService.getCustomerById(Long.parseLong(newVisa.getPaidCustomerUI())));
    }
    private void calculateProfit(VisaDto newVisa) {
        // check if the price is null
        if (newVisa.getPerchesPrice() == null) {
            newVisa.setPerchesPrice(BigDecimal.ZERO);
        }
        if (newVisa.getSalesPrice() == null) {
            newVisa.setSalesPrice(BigDecimal.ZERO);
        }
        // calculate profit
        newVisa.setProfit(newVisa.getSalesPrice().subtract(newVisa.getPerchesPrice()));
    }

    private void calculateCustomerDebt(VisaDto newVisa) {
        // check if the price is null
        if (newVisa.getSalesPrice() == null) {
            newVisa.setSalesPrice(BigDecimal.ZERO);
        }
        // calculate customer debt
        CustomerDto customer = customerService.findById(newVisa.getCustomer().getId()); // find customer

        if (newVisa.getCurrencyUnit().equals(CurrencyUnits.TRY)) {
            customer.setCustomerTRYBalance(customer.getCustomerTRYBalance().subtract(newVisa.getSalesPrice()));
        }
        else if (newVisa.getCurrencyUnit().equals(CurrencyUnits.USD)) {
            customer.setCustomerUSDBalance(customer.getCustomerUSDBalance().subtract(newVisa.getSalesPrice()));
        }
        if (newVisa.getCurrencyUnit().equals(CurrencyUnits.EUR)) {
            customer.setCustomerEURBalance(customer.getCustomerEURBalance().subtract(newVisa.getSalesPrice()));
        }
        customerService.save(customer); // save customer
    }

    private void calculateCreditCardDebt(VisaDto newVisa) {
        // check if the price is null
        if (newVisa.getSalesPrice() == null) {
            newVisa.setSalesPrice(BigDecimal.ZERO);
        }
        // calculate credit card debt
        CardDto creditCard = cardService.findById(newVisa.getPaidCard().getId()); // find credit card

        if (newVisa.getCurrencyUnit().equals(CurrencyUnits.TRY)) {
            creditCard.setAvailableLimitTRY(creditCard.getAvailableLimitTRY().subtract(newVisa.getPerchesPrice()));
        }
        else if (newVisa.getCurrencyUnit().equals(CurrencyUnits.USD)) {
            creditCard.setAvailableLimitUSD(creditCard.getAvailableLimitUSD().subtract(newVisa.getPerchesPrice()));
        }
        if (newVisa.getCurrencyUnit().equals(CurrencyUnits.EUR)) {
            creditCard.setAvailableLimitEUR(creditCard.getAvailableLimitEUR().subtract(newVisa.getSalesPrice()));
        }
        cardService.saveCreditCard(creditCard); // save credit card
    }

}
