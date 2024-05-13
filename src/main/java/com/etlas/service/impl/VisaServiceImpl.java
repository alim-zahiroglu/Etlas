package com.etlas.service.impl;

import com.etlas.dto.CardDto;
import com.etlas.dto.CustomerDto;
import com.etlas.dto.UserDto;
import com.etlas.dto.VisaDto;
import com.etlas.entity.Customer;
import com.etlas.entity.Visa;
import com.etlas.entity.VisaType;
import com.etlas.enums.CountriesTr;
import com.etlas.enums.CurrencyUnits;
import com.etlas.mapper.MapperUtil;
import com.etlas.repository.VisaRepository;
import com.etlas.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisaServiceImpl implements VisaService {
    private final CustomerService customerService;
    private final CardService cardService;
    private final SecurityService securityService;
    private final VisaTypeService visaTypeService;
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
        List<Visa> visaList = repository.findAllByIsDeletedOrderByLastUpdateDateTimeDesc(false);
        if (visaList.isEmpty()) return List.of();
        return visaList.stream()
                .map(visa -> mapper.convert(visa, new VisaDto()))
                .toList();
    }

    @Override
    public VisaDto findById(long visaId) {
        Visa visa = repository.findByIdAndIsDeletedFalse(visaId).orElseThrow(() -> new IllegalArgumentException("Visa not found"));
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
    public BindingResult validateNewVisa(VisaDto newVisa, BindingResult bindingResult) {
        if (newVisa.getCustomerUI().equals("0")) {
            bindingResult.rejectValue("customerUI", "error.visa", "Please select a customer");
        }
        if (newVisa.getPaidCustomerUI().equals("0")) {
            bindingResult.rejectValue("paidCustomerUI", "error.visa", "Please select a paid customer");
        }
        return bindingResult;
    }

    @Override
    public boolean isUserBoughtTicket(String userName) {
        return repository.existsByBoughtUser_UserNameAndIsDeleted(userName, false);
    }

    @Override
    public VisaDto saveNewVisa(VisaDto newVisa) {
        saveNewVisaTypeIfAdded(newVisa);
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

    private void saveNewVisaTypeIfAdded(VisaDto newVisa) {
        String newVisaType = newVisa.getVisaType();
        VisaType visaType = visaTypeService.findByName(newVisaType);
        if (visaType == null) {
            VisaType newType = VisaType.builder().name(newVisaType).build();
            visaTypeService.save(newType);
        }
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
        CustomerDto customer = customerService.findById(newVisa.getPaidCustomer().getId()); // find customer

        if (newVisa.getCurrencyUnit().equals(CurrencyUnits.TRY)) {
            customer.setCustomerTRYBalance(customer.getCustomerTRYBalance().subtract(newVisa.getSalesPrice()));
        } else if (newVisa.getCurrencyUnit().equals(CurrencyUnits.USD)) {
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
        } else if (newVisa.getCurrencyUnit().equals(CurrencyUnits.USD)) {
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
        saveNewVisaTypeIfAdded(visaToBeUpdate);
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
        VisaDto oldVisa = findById(oldVisaId);
        CurrencyUnits currencyUnit = oldVisa.getCurrencyUnit();
        BigDecimal salesPrice = oldVisa.getSalesPrice();
        CustomerDto oldPaidCustomer = customerService.findById(oldVisa.getPaidCustomer().getId()); // find customer

        // remove old customer debt

        if (currencyUnit.equals(CurrencyUnits.TRY)) {
            oldPaidCustomer.setCustomerTRYBalance(oldPaidCustomer.getCustomerTRYBalance().add(salesPrice));
        } else if (currencyUnit.equals(CurrencyUnits.USD)) {
            oldPaidCustomer.setCustomerUSDBalance(oldPaidCustomer.getCustomerUSDBalance().add(salesPrice));
        }
        if (currencyUnit.equals(CurrencyUnits.EUR)) {
            oldPaidCustomer.setCustomerEURBalance(oldPaidCustomer.getCustomerEURBalance().add(salesPrice));
        }
        customerService.save(oldPaidCustomer); // save customer
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

    @Override
    public void deleteVisa(long visaId) {
        Visa visaToBeDelete = repository.findById(visaId)
                .orElseThrow(() -> new IllegalArgumentException("Visa not found"));
        visaToBeDelete.setDeleted(true);
        repository.save(visaToBeDelete);
    }

    @Override
    public List<String> getAllUniqueVisTypeWithCountry() {
        List<Object[]> visaList = repository.getAllUniqueVisaTypeAndCountry(false);
        if (!visaList.isEmpty()) {
            return visaList.stream()
                    .map(objects -> {
                                CountriesTr country = CountriesTr.valueOf((String) objects[0]);
                                return country.getName() + ", " + objects[1];
                            }
                    )
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    @Override
    public List<String> getAllUniqueVisTypeWithCountryFromCustomer(long customerId) {
        List<Object[]> visaList = repository.getAllUniqueVisaTypeAndCountryFromCustomer(customerId, false);
        if (!visaList.isEmpty()) {
            return visaList.stream()
                    .map(objects -> {
                                CountriesTr country = CountriesTr.valueOf((String) objects[0]);
                                return country.getName() + ", " + objects[1];
                            }
                    )
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    @Override
    public boolean isCustomerHasVisa(Customer customer) {
        return repository.existsByCustomerIdOrPaidCustomerIdAndIsDeleted(customer.getId(), customer.getId(), false);
    }

    @Override
    public boolean isCardUsedInAnyVisa(String cardId) {
        return repository.existsByPaidCardIdAndIsDeleted(Long.parseLong(cardId), false);
    }

    @Override
    public BigDecimal getVisaTRYTotalPerches(String time) {
        BigDecimal result;
        if (Objects.equals(time, "LastMonth")) {
            result = repository.getVisaTotalPerchesByMonth(CurrencyUnits.TRY, LocalDate.now().getMonth().minus(1).getValue(),false);
        } else if (Objects.equals(time, "thisYear")) {

            result = repository.getVisaTotalPerchesByYear(CurrencyUnits.TRY, LocalDate.now().getYear(),false);
        } else if (Objects.equals(time, "lastYear")) {
            result = repository.getVisaTotalPerchesByYear(CurrencyUnits.TRY, LocalDate.now().getYear() - 1,false);
        } else if (Objects.equals(time, "all")) {
            result = repository.getVisaTotalPerches(CurrencyUnits.TRY,false);
        } else {
            result = repository.getVisaTotalPerchesByMonth(CurrencyUnits.TRY, LocalDate.now().getMonthValue(),false);
        }
        return result == null ? BigDecimal.ZERO : result;
    }

    @Override
    public BigDecimal getVisaUSDTotalPerches(String time) {
        BigDecimal result;
        if (Objects.equals(time, "LastMonth")) {
            result = repository.getVisaTotalPerchesByMonth(CurrencyUnits.USD, LocalDate.now().getMonth().minus(1).getValue(),false);
        } else if (Objects.equals(time, "thisYear")) {

            result = repository.getVisaTotalPerchesByYear(CurrencyUnits.USD, LocalDate.now().getYear(),false);
        } else if (Objects.equals(time, "lastYear")) {
            result = repository.getVisaTotalPerchesByYear(CurrencyUnits.USD, LocalDate.now().getYear() - 1,false);
        } else if (Objects.equals(time, "all")) {
            result = repository.getVisaTotalPerches(CurrencyUnits.USD,false);
        } else {
            result = repository.getVisaTotalPerchesByMonth(CurrencyUnits.USD, LocalDate.now().getMonthValue(),false);
        }
        return result == null ? BigDecimal.ZERO : result;
    }

    @Override
    public BigDecimal getVisaEURTotalPerches(String time) {
        BigDecimal result;
        if (Objects.equals(time, "LastMonth")) {
            result = repository.getVisaTotalPerchesByMonth(CurrencyUnits.EUR, LocalDate.now().getMonth().minus(1).getValue(),false);
        } else if (Objects.equals(time, "thisYear")) {

            result = repository.getVisaTotalPerchesByYear(CurrencyUnits.EUR, LocalDate.now().getYear(),false);
        } else if (Objects.equals(time, "lastYear")) {
            result = repository.getVisaTotalPerchesByYear(CurrencyUnits.EUR, LocalDate.now().getYear() - 1,false);
        } else if (Objects.equals(time, "all")) {
            result = repository.getVisaTotalPerches(CurrencyUnits.EUR,false);
        } else {
            result = repository.getVisaTotalPerchesByMonth(CurrencyUnits.EUR, LocalDate.now().getMonthValue(),false);
        }
        return result == null ? BigDecimal.ZERO : result;
    }

    @Override
    public BigDecimal getVisaTRYTotalSales(String time) {
        BigDecimal result;
        if (Objects.equals(time, "LastMonth")) {
            result = repository.getVisaTotalSalesByMonth(CurrencyUnits.TRY, LocalDate.now().getMonth().minus(1).getValue(),false);
        } else if (Objects.equals(time, "thisYear")) {

            result = repository.getVisaTotalSalesByYear(CurrencyUnits.TRY, LocalDate.now().getYear(),false);
        } else if (Objects.equals(time, "lastYear")) {
            result = repository.getVisaTotalSalesByYear(CurrencyUnits.TRY, LocalDate.now().getYear() - 1,false);
        } else if (Objects.equals(time, "all")) {
            result = repository.getVisaTotalSales(CurrencyUnits.TRY,false);
        } else {
            result = repository.getVisaTotalSalesByMonth(CurrencyUnits.TRY, LocalDate.now().getMonthValue(),false);
        }
        return result == null ? BigDecimal.ZERO : result;
    }

    @Override
    public BigDecimal getVisaUSDTotalSales(String time) {
        BigDecimal result;
        if (Objects.equals(time, "LastMonth")) {
            result = repository.getVisaTotalSalesByMonth(CurrencyUnits.USD, LocalDate.now().getMonth().minus(1).getValue(),false);
        } else if (Objects.equals(time, "thisYear")) {

            result = repository.getVisaTotalSalesByYear(CurrencyUnits.USD, LocalDate.now().getYear(),false);
        } else if (Objects.equals(time, "lastYear")) {
            result = repository.getVisaTotalSalesByYear(CurrencyUnits.USD, LocalDate.now().getYear() - 1,false);
        } else if (Objects.equals(time, "all")) {
            result = repository.getVisaTotalSales(CurrencyUnits.USD,false);
        } else {
            result = repository.getVisaTotalSalesByMonth(CurrencyUnits.USD, LocalDate.now().getMonthValue(),false);
        }
        return result == null ? BigDecimal.ZERO : result;
    }

    @Override
    public BigDecimal getVisaEURTotalSales(String time) {
        BigDecimal result;
        if (Objects.equals(time, "LastMonth")) {
            result = repository.getVisaTotalSalesByMonth(CurrencyUnits.EUR, LocalDate.now().getMonth().minus(1).getValue(),false);
        } else if (Objects.equals(time, "thisYear")) {

            result = repository.getVisaTotalSalesByYear(CurrencyUnits.EUR, LocalDate.now().getYear(),false);
        } else if (Objects.equals(time, "lastYear")) {
            result = repository.getVisaTotalSalesByYear(CurrencyUnits.EUR, LocalDate.now().getYear() - 1,false);
        } else if (Objects.equals(time, "all")) {
            result = repository.getVisaTotalSales(CurrencyUnits.EUR,false);
        } else {
            result = repository.getVisaTotalSalesByMonth(CurrencyUnits.EUR, LocalDate.now().getMonthValue(),false);
        }
        return result == null ? BigDecimal.ZERO : result;
    }


    @Override
    public BigDecimal getVisaTRYTotalProfit(String time) {
        BigDecimal result;
        if (Objects.equals(time, "LastMonth")) {
            result = repository.getVisaTotalProfitByMonth(CurrencyUnits.TRY, LocalDate.now().getMonth().minus(1).getValue(),false);
        } else if (Objects.equals(time, "thisYear")) {

            result = repository.getVisaTotalProfitByYear(CurrencyUnits.TRY, LocalDate.now().getYear(),false);
        } else if (Objects.equals(time, "lastYear")) {
            result = repository.getVisaTotalProfitByYear(CurrencyUnits.TRY, LocalDate.now().getYear() - 1,false);
        } else if (Objects.equals(time, "all")) {
            result = repository.getVisaTotalProfit(CurrencyUnits.TRY,false);
        } else {
            result = repository.getVisaTotalProfitByMonth(CurrencyUnits.TRY, LocalDate.now().getMonthValue(),false);
        }
        return result == null ? BigDecimal.ZERO : result;
    }

    @Override
    public BigDecimal getVisaUSDTotalProfit(String time) {
        BigDecimal result;
        if (Objects.equals(time, "LastMonth")) {
            result = repository.getVisaTotalProfitByMonth(CurrencyUnits.USD, LocalDate.now().getMonth().minus(1).getValue(),false);
        } else if (Objects.equals(time, "thisYear")) {

            result = repository.getVisaTotalProfitByYear(CurrencyUnits.USD, LocalDate.now().getYear(),false);
        } else if (Objects.equals(time, "lastYear")) {
            result = repository.getVisaTotalProfitByYear(CurrencyUnits.USD, LocalDate.now().getYear() - 1,false);
        } else if (Objects.equals(time, "all")) {
            result = repository.getVisaTotalProfit(CurrencyUnits.USD,false);
        } else {
            result = repository.getVisaTotalProfitByMonth(CurrencyUnits.USD, LocalDate.now().getMonthValue(),false);
        }
        return result == null ? BigDecimal.ZERO : result;
    }

    @Override
    public BigDecimal getVisaEURTotalProfit(String time) {
        BigDecimal result;
        if (Objects.equals(time, "LastMonth")) {
            result = repository.getVisaTotalProfitByMonth(CurrencyUnits.EUR, LocalDate.now().getMonth().minus(1).getValue(),false);
        } else if (Objects.equals(time, "thisYear")) {

            result = repository.getVisaTotalProfitByYear(CurrencyUnits.EUR, LocalDate.now().getYear(),false);
        } else if (Objects.equals(time, "lastYear")) {
            result = repository.getVisaTotalProfitByYear(CurrencyUnits.EUR, LocalDate.now().getYear() - 1,false);
        } else if (Objects.equals(time, "all")) {
            result = repository.getVisaTotalProfit(CurrencyUnits.EUR,false);
        } else {
            result = repository.getVisaTotalProfitByMonth(CurrencyUnits.EUR, LocalDate.now().getMonthValue(),false);
        }
        return result == null ? BigDecimal.ZERO : result;
    }


    @Override
    public int getTotalTRYPerchesVisa(String time) {
        Integer result;
        if (Objects.equals(time, "LastMonth")) {
            result = repository.getTotalPerchesVisaByMonth(CurrencyUnits.TRY, LocalDate.now().getMonth().minus(1).getValue(),false);
        }
        else if (Objects.equals(time, "thisYear")) {

            result = repository.getTotalPerchesVisaByYear(CurrencyUnits.TRY, LocalDate.now().getYear(),false);
        }
        else if (Objects.equals(time, "lastYear")) {
            result =  repository.getTotalPerchesVisaByYear(CurrencyUnits.TRY, LocalDate.now().getYear()-1,false);
        }
        else if (Objects.equals(time, "all")) {
            result = repository.getTotalPerchesVisa(CurrencyUnits.TRY,false);
        }
        else {result = repository.getTotalPerchesVisaByMonth(CurrencyUnits.TRY, LocalDate.now().getMonthValue(),false);}

        return result == null ? 0 : result;
    }

    @Override
    public int getTotalUSDPerchesVisa(String time) {
        Integer result;
        if (Objects.equals(time, "LastMonth")) {
            result = repository.getTotalPerchesVisaByMonth(CurrencyUnits.USD, LocalDate.now().getMonth().minus(1).getValue(),false);
        }
        else if (Objects.equals(time, "thisYear")) {

            result = repository.getTotalPerchesVisaByYear(CurrencyUnits.USD, LocalDate.now().getYear(),false);
        }
        else if (Objects.equals(time, "lastYear")) {
            result =  repository.getTotalPerchesVisaByYear(CurrencyUnits.USD, LocalDate.now().getYear()-1,false);
        }
        else if (Objects.equals(time, "all")) {
            result = repository.getTotalPerchesVisa(CurrencyUnits.USD,false);
        }
        else {result = repository.getTotalPerchesVisaByMonth(CurrencyUnits.USD, LocalDate.now().getMonthValue(),false);}

        return result == null ? 0 : result;
    }

    @Override
    public int getTotalEURPerchesVisa(String time) {
        Integer result;
        if (Objects.equals(time, "LastMonth")) {
            result = repository.getTotalPerchesVisaByMonth(CurrencyUnits.EUR, LocalDate.now().getMonth().minus(1).getValue(),false);
        }
        else if (Objects.equals(time, "thisYear")) {

            result = repository.getTotalPerchesVisaByYear(CurrencyUnits.EUR, LocalDate.now().getYear(),false);
        }
        else if (Objects.equals(time, "lastYear")) {
            result =  repository.getTotalPerchesVisaByYear(CurrencyUnits.EUR, LocalDate.now().getYear()-1,false);
        }
        else if (Objects.equals(time, "all")) {
            result = repository.getTotalPerchesVisa(CurrencyUnits.EUR,false);
        }
        else {result = repository.getTotalPerchesVisaByMonth(CurrencyUnits.EUR, LocalDate.now().getMonthValue(),false);}

        return result == null ? 0 : result;
    }
}

