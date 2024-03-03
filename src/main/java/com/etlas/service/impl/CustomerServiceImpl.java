package com.etlas.service.impl;

import com.etlas.dto.CustomerDto;
import com.etlas.entity.Customer;
import com.etlas.mapper.MapperUtil;
import com.etlas.repository.CustomerRepository;
import com.etlas.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repository;
    private final MapperUtil mapper;

    @Override
    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = repository.findAllByIsDeleted(false);
        return customers.stream()
                .map(customer -> mapper.convert(customer, new CustomerDto()))
                .toList();
    }
}
