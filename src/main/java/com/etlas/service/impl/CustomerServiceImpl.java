package com.etlas.service.impl;

import com.etlas.dto.CustomerDto;
import com.etlas.entity.Customer;
import com.etlas.enums.CountriesTr;
import com.etlas.enums.CurrencyUnits;
import com.etlas.enums.CustomerType;
import com.etlas.exception.CustomerNotFoundException;
import com.etlas.mapper.MapperUtil;
import com.etlas.repository.CustomerRepository;
import com.etlas.service.BalanceService;
import com.etlas.service.CustomerService;
import com.etlas.service.TicketService;
import com.etlas.service.VisaService;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
//@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repository;
    private final TicketService ticketService;
    private final MapperUtil mapper;
    private final VisaService visaService;
    private final BalanceService balanceService;

    public CustomerServiceImpl(CustomerRepository repository, @Lazy TicketService ticketService, MapperUtil mapper, @Lazy VisaService visaService, @Lazy BalanceService balanceService) {
        this.repository = repository;
        this.ticketService = ticketService;
        this.mapper = mapper;
        this.visaService = visaService;
        this.balanceService = balanceService;
    }

    @Override
    public CustomerDto initializeNewCustomer() {
        return CustomerDto.builder()
                .Individual(true)
                .country(CountriesTr.TUR)
                .build();
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = repository.findAllByIsDeletedOrderByLastUpdateDateTimeDesc(false);
        return customers.stream()
                .map(customer -> mapper.convert(customer, new CustomerDto()))
                .toList();
    }

    @Override
    public CustomerDto findById(long id) {
        Customer customer =  repository.findById(id).orElseThrow(()-> new CustomerNotFoundException("No customer found with id: " + id));
        return mapper.convert(customer,new CustomerDto());
    };

    @Override
    public List<CustomerDto> getAllIndividualCustomers() {
        List<Customer> customers = repository.findAllByCustomerTypeAndIsDeleted(CustomerType.INDIVIDUAL,false);
        return customers.stream()
                .map(customer -> mapper.convert(customer, new CustomerDto()))
                .toList();
    }

    @Transactional
    @Override
    public void save(CustomerDto customer) {
        repository.save(mapper.convert(customer, new Customer()));
    }

    @Transactional
    @Override
    public CustomerDto saveNewCustomer(CustomerDto newCustomer) {
        CustomerDto customerToBeSave;
        if (newCustomer.isCompany()) {
            customerToBeSave = adjustNewCustomerCompany(newCustomer);

        } else {
            customerToBeSave = adjustNewCustomerIndividual(newCustomer);
        }

        Customer savedCustomer = repository.save(mapper.convert(customerToBeSave, new Customer()));
        return mapper.convert(savedCustomer, new CustomerDto());
    }

    private CustomerDto adjustNewCustomerCompany(CustomerDto newCompany) {
        newCompany.setCustomerType(CustomerType.COMPANY);
        if (newCompany.getCustomerTRYBalance() == null) newCompany.setCustomerTRYBalance(BigDecimal.ZERO);
        if (newCompany.getCustomerUSDBalance() == null) newCompany.setCustomerUSDBalance(BigDecimal.ZERO);
        if (newCompany.getCustomerEURBalance() == null) newCompany.setCustomerEURBalance(BigDecimal.ZERO);
        newCompany.setFirstName(null);
        newCompany.setLastName(null);
        newCompany.setGender(null);
        return newCompany;
    }

    private CustomerDto adjustNewCustomerIndividual(CustomerDto newCompany) {
        newCompany.setCustomerType(CustomerType.INDIVIDUAL);
        if (newCompany.getCustomerTRYBalance() == null) newCompany.setCustomerTRYBalance(BigDecimal.ZERO);
        if (newCompany.getCustomerUSDBalance() == null) newCompany.setCustomerUSDBalance(BigDecimal.ZERO);
        if (newCompany.getCustomerEURBalance() == null) newCompany.setCustomerEURBalance(BigDecimal.ZERO);
        newCompany.setCompanyName(null);
        newCompany.setOfficeNumber(null);
        return newCompany;
    }

    @Transactional
    @Override
    public CustomerDto deleteCustomer(long customerId) {
        Customer customerToBeDelete = repository.findById(customerId)
                .orElseThrow(()-> new CustomerNotFoundException("No customer found with id: " + customerId));
        if (customerToBeDelete != null) {
            customerToBeDelete.setDeleted(true);
            repository.save(customerToBeDelete);
            return mapper.convert(customerToBeDelete, new CustomerDto());
        }
        return null;
    }

    @Override
    public boolean isCustomerDeletable(long customerId) {
        Customer customer = repository.findByIdAndIsDeleted(customerId,false);
        if (customer != null) {
            boolean isCustomerHasTicket = ticketService.isCustomerHasTickets(customer);
            boolean isCustomerHasVisa = visaService.isCustomerHasVisa(customer);
            boolean isCustomerHasBalanceRecord = balanceService.isCustomerHasBalanceRecord(customer);
            return customer.getCustomerUSDBalance().compareTo(BigDecimal.ZERO) >= 0 &&
                    customer.getCustomerTRYBalance().compareTo(BigDecimal.ZERO) >= 0 &&
                    customer.getCustomerEURBalance().compareTo(BigDecimal.ZERO) >= 0 && !isCustomerHasTicket
                    && !isCustomerHasVisa && !isCustomerHasBalanceRecord;
        }
        return true;
    }

    @Override
    public CustomerDto getCustomerById(long customerId) {
        Customer foundCustomer = repository.findById(customerId)
                .orElseThrow(()-> new CustomerNotFoundException("No customer found with id: " + customerId));
        return mapper.convert(foundCustomer, new CustomerDto());
    }

    @Transactional
    @Override
    public CustomerDto saveUpdatedCustomer(CustomerDto customerToBeUpdate) {
        CustomerDto customerToUpdate = adjustBalances(customerToBeUpdate);
        Customer savedCustomer = repository.save(mapper.convert(customerToUpdate, new Customer()));
        return mapper.convert(savedCustomer, new CustomerDto());
    }

    private CustomerDto adjustBalances(CustomerDto customerDto) {
        if (customerDto.getCustomerTRYBalance() == null) customerDto.setCustomerTRYBalance(BigDecimal.ZERO);
        if (customerDto.getCustomerUSDBalance() == null) customerDto.setCustomerUSDBalance(BigDecimal.ZERO);
        if (customerDto.getCustomerEURBalance() == null) customerDto.setCustomerEURBalance(BigDecimal.ZERO);
        return customerDto;
    }

    @Override
    public BindingResult checkNewCustomerValidation(CustomerDto newCustomer, BindingResult bindingResult) {
        CustomerDto customerDto = new CustomerDto();
        BindingResult newBindingResult = new BeanPropertyBindingResult(customerDto, "customerDto");
        if (newCustomer.isCompany()) {

            if (repository.existsByCompanyName(newCustomer.getCompanyName())) {
                bindingResult.addError(new FieldError("newCustomer", "companyName", "this Customer already exist"));
            }
            List<String> fieldsToExclude = List.of("firstName");

            removeFieldErrors(bindingResult, newBindingResult, fieldsToExclude);

            return newBindingResult;

        } else if (newCustomer.isIndividual()) {

            List<String> fieldsToExclude = List.of("companyName");

            removeFieldErrors(bindingResult, newBindingResult, fieldsToExclude);
            return newBindingResult;
            }
        return bindingResult;
    }

    private void removeFieldErrors(BindingResult sourceResult, BindingResult targetResult, List<String> fieldsToExclude) {
        List<FieldError> errorsToKeep = sourceResult.getFieldErrors().stream()
                .filter(fer -> !fieldsToExclude.contains(fer.getField()))
                .toList();

        for (FieldError fieldError : errorsToKeep) {
            targetResult.addError(fieldError);
        }
    }

    @Override
    public BindingResult validateUpdateCustomer(CustomerDto customerToBeUpdate, BindingResult bindingResult) {
        CustomerDto customerDto = new CustomerDto();
        BindingResult newBindingResult = new BeanPropertyBindingResult(customerDto, "customerDto");
        if (customerToBeUpdate.getCustomerType().getDescription().equals("Company")){
            if (repository.existsByCompanyNameAndIdNot(customerToBeUpdate.getCompanyName(),customerToBeUpdate.getId())) {
                bindingResult.addError(new FieldError("newCustomer", "companyName", "this Customer already exist"));
            }

            removeFieldErrors(bindingResult, newBindingResult, List.of("firstName"));
            return newBindingResult;
        }else if (customerToBeUpdate.getCustomerType().getDescription().equals("Individual")){
            removeFieldErrors(bindingResult, newBindingResult, List.of("companyName"));
            return newBindingResult;
        }
        return bindingResult;
    }

    @Transactional
    @Override
    public void saveCustomer(CustomerDto customer) {
        repository.save(mapper.convert(customer, new Customer()));

    }

    @Transactional
    @Override
    public void saveNewCustomerIfAdded(long customerId) {
        if (customerId == 0) return;
        Customer customer = repository.findById(customerId).orElseThrow( ()-> new CustomerNotFoundException("No customer found with id: " + customerId));
        if (customer == null) return;
        customer.setDeleted(false);
        repository.save(customer);
    }

    @Override
    public BigDecimal getTotalTRYUnpaid() {
        BigDecimal result = repository.getTotalTRYDebit(false);
        return result == null ? BigDecimal.ZERO : result.negate();
    }

    @Override
    public BigDecimal getTotalUSDUnpaid() {
        BigDecimal result = repository.getTotalUSDDebit(false);
        return result == null ? BigDecimal.ZERO : result.negate();
    }

    @Override
    public BigDecimal getTotalEURUnpaid() {
        BigDecimal result = repository.getTotalEURDebit(false);
        return result == null ? BigDecimal.ZERO : result.negate();
    }
}
