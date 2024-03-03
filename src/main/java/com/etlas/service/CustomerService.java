package com.etlas.service;

import com.etlas.dto.CustomerDto;

import java.util.List;

public interface CustomerService {
    List<CustomerDto> getAllCustomers();

    CustomerDto saveNewCustomer(CustomerDto newCustomer);
}
