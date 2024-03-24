package com.etlas.service.impl;

import com.etlas.dto.CustomerDto;
import com.etlas.entity.Customer;
import com.etlas.enums.CountriesTr;
import com.etlas.enums.CustomerType;
import com.etlas.mapper.MapperUtil;
import com.etlas.repository.CustomerRepository;
import com.etlas.service.CustomerService;
import com.etlas.service.TicketService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
//@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repository;
    private final TicketService ticketService;
    private final MapperUtil mapper;

    public CustomerServiceImpl(CustomerRepository repository, @Lazy TicketService ticketService, MapperUtil mapper) {
        this.repository = repository;
        this.ticketService = ticketService;
        this.mapper = mapper;
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
        Customer customer =  repository.findById(id).orElseThrow(NoSuchElementException::new);
        return mapper.convert(customer,new CustomerDto());
    };

    @Override
    public List<CustomerDto> getAllIndividualCustomers() {
        List<Customer> customers = repository.findAllByCustomerTypeAndIsDeleted(CustomerType.INDIVIDUAL,false);
        return customers.stream()
                .map(customer -> mapper.convert(customer, new CustomerDto()))
                .toList();
    }

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

    @Override
    public CustomerDto deleteCustomer(long customerId) {
        Customer customerToBeDelete = repository.findById(customerId)
                .orElseThrow(NoSuchElementException::new);
        if (customerToBeDelete != null) {
            customerToBeDelete.setDeleted(true);
            repository.save(customerToBeDelete);
            return mapper.convert(customerToBeDelete, new CustomerDto());
        }
        return null;
    }

    @Override
    public boolean isCustomerDeletable(long customerId) {
        Customer customer = repository.findById(customerId)
                .orElseThrow(NoSuchElementException::new);
        if (customer != null) {
            boolean isCustomerHasTicket = ticketService.isCustomerHasTickets(customer);
            // TODO check if customer has visa
            return customer.getCustomerUSDBalance().compareTo(BigDecimal.ZERO) >= 0 &&
                    customer.getCustomerTRYBalance().compareTo(BigDecimal.ZERO) >= 0 &&
                    customer.getCustomerEURBalance().compareTo(BigDecimal.ZERO) >= 0 && !isCustomerHasTicket;
        }
        return true;
    }

    @Override
    public CustomerDto getCustomerById(long customerId) {
        Customer foundCustomer = repository.findById(customerId)
                .orElseThrow(NoSuchElementException::new);
        return mapper.convert(foundCustomer, new CustomerDto());
    }

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
}
