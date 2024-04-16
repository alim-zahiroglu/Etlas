package com.etlas.service.impl;

import com.etlas.dto.CardDto;
import com.etlas.dto.CustomerDto;
import com.etlas.dto.UserDto;
import com.etlas.dto.VisaDto;
import com.etlas.entity.Visa;
import com.etlas.enums.CountriesTr;
import com.etlas.enums.CurrencyUnits;
import com.etlas.mapper.MapperUtil;
import com.etlas.repository.VisaRepository;
import com.etlas.service.CardService;
import com.etlas.service.CustomerService;
import com.etlas.service.SecurityService;
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
    private final SecurityService securityService;
    private final VisaRepository repository;
    private final MapperUtil mapper;

    @Override
    public VisaDto initializeVisa() {
        UserDto currentUser = securityService.getLoggedInUser();
        return VisaDto.builder()
                .visaType("ömre vizasi")
                .currencyUnit(CurrencyUnits.USD)
                .country(CountriesTr.SAU)
                .perchesPrice(BigDecimal.ZERO)
                .salesPrice(BigDecimal.ZERO)
                .boughtUser(currentUser)
                .build();
    }

    @Override
    public List<VisaDto> getAllVisas() {
        return  repository.findAllByIsDeletedFalse().stream()
                .map(visa -> mapper.convert(visa, new VisaDto()))
                .toList();
    }

    @Override
    public VisaDto findById(long visaId) {
        Visa visa = repository.findByIdAndIsDeletedFalse(visaId).orElseThrow( () -> new IllegalArgumentException("Visa not found"));
        return mapper.convert(visa, new VisaDto());
    }

    @Override
    public VisaDto adjustNewVisa(VisaDto newVisa, String addedCustomerId) {
       CustomerDto customer = customerService.getCustomerById(Long.parseLong(addedCustomerId));
       newVisa.setCustomer(customer);
       newVisa.setPaidCustomer(customer);
       newVisa.setCustomerUI(addedCustomerId);
       newVisa.setPaidCustomerUI(addedCustomerId);
       return newVisa;
    }

    @Override
    public VisaDto saveNewVisa(VisaDto newVisa) {
        // save new customer if new customer added
        customerService.saveNewCustomerIfAdded(Long.parseLong(newVisa.getCustomerUI()));
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

    @Override
    public VisaDto prepareVisaToUpdate(VisaDto visa) {
        visa.setCustomerUI(String.valueOf(visa.getCustomer().getId()));
        visa.setPaidCustomerUI(String.valueOf(visa.getPaidCustomer().getId()));
        return visa;
    }

    @Override
    public void saveUpdatedVisa(VisaDto visaToBeUpdate) {
        // save new customer if new customer added
        customerService.saveNewCustomerIfAdded(Long.parseLong(visaToBeUpdate.getCustomerUI()));

        //set customer and paid customer
        setCustomerAndPaidCustomer(visaToBeUpdate);

        // calculate profit
        calculateProfit(visaToBeUpdate);

        // remove old customer debt
        removeOldCustomerDebt(visaToBeUpdate.getId());

        // calculate customer debt
        calculateCustomerDebt(visaToBeUpdate);

        // remove old credit card debt
        removeOldCreditCardDebt(visaToBeUpdate.getId());

        // calculate credit card debt
        calculateCreditCardDebt(visaToBeUpdate);

        // save updated visa
        Visa savedVisa = repository.save(mapper.convert(visaToBeUpdate, new Visa()));
    }

    private void removeOldCustomerDebt(long oldVisaId) {
        VisaDto oldVisa =  findById(oldVisaId);
        CurrencyUnits currencyUnit = oldVisa.getCurrencyUnit();
        BigDecimal salesPrice = oldVisa.getSalesPrice();
        CustomerDto oldCustomer = customerService.findById(oldVisa.getCustomer().getId()); // find customer

        // remove old customer debt

        if (currencyUnit.equals(CurrencyUnits.TRY)) {
            oldCustomer.setCustomerTRYBalance(oldCustomer.getCustomerTRYBalance().add(salesPrice));
        } else if (currencyUnit.equals(CurrencyUnits.USD)) {
            oldCustomer.setCustomerUSDBalance(oldCustomer.getCustomerUSDBalance().add(salesPrice));
        }
        if (currencyUnit.equals(CurrencyUnits.EUR)) {
            oldCustomer.setCustomerEURBalance(oldCustomer.getCustomerEURBalance().add(salesPrice));
        }
        customerService.save(oldCustomer); // save customer
    }

    private void removeOldCreditCardDebt(long oldVisaId) {
        VisaDto oldVisa = findById(oldVisaId);
        CurrencyUnits currencyUnit = oldVisa.getCurrencyUnit();
        BigDecimal perchesPrice = oldVisa.getPerchesPrice();
        CardDto oldCreditCard = cardService.findById(oldVisa.getPaidCard().getId()); // find credit card

        // remove old credit card debt

        if (currencyUnit.equals(CurrencyUnits.TRY)) {
            oldCreditCard.setAvailableLimitTRY(oldCreditCard.getAvailableLimitTRY().add(perchesPrice));
        } else if (currencyUnit.equals(CurrencyUnits.USD)) {
            oldCreditCard.setAvailableLimitUSD(oldCreditCard.getAvailableLimitUSD().add(perchesPrice));
        }
        if (currencyUnit.equals(CurrencyUnits.EUR)) {
            oldCreditCard.setAvailableLimitEUR(oldCreditCard.getAvailableLimitEUR().add(perchesPrice));
        }
        cardService.saveCreditCard(oldCreditCard); // save credit card
    }
}
