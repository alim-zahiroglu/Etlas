package com.etlas.service;

import com.etlas.dto.CustomerDto;
import com.etlas.dto.UserDto;

import java.util.List;

public interface CustomerService {
    List<CustomerDto> getAllCustomers();

    CustomerDto saveNewCustomer(CustomerDto newCustomer);

    CustomerDto deleteCustomer(long customerId);
}
