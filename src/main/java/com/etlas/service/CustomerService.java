package com.etlas.service;

import com.etlas.dto.CustomerDto;
import com.etlas.entity.Customer;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.List;

public interface CustomerService {
    List<CustomerDto> getAllCustomers();

    CustomerDto saveNewCustomer(CustomerDto newCustomer);

    CustomerDto deleteCustomer(long customerId);

    CustomerDto getCustomerById(long customerId);

    CustomerDto saveUpdatedCustomer(CustomerDto customerToBeUpdate);

    BindingResult checkNewCustomerValidation(CustomerDto newCustomer, BindingResult bindingResult);

    BindingResult validateUpdateCustomer(CustomerDto customerToBeUpdate, BindingResult bindingResult);

    List<CustomerDto> getAllIndividualCustomers();

    CustomerDto findById(long id);

    CustomerDto initializeNewCustomer();

    boolean isCustomerDeletable(long customerId);

    void save(CustomerDto customer);

    void saveNewCustomerIfAdded(long customerId);

    void saveCustomer(CustomerDto customer);

    BigDecimal getTotalTRYUnpaid();

    BigDecimal getTotalUSDUnpaid();

    BigDecimal getTotalEURUnpaid();
}
