package com.etlas.service.impl;

import com.etlas.dto.CustomerDto;
import com.etlas.entity.Customer;
import com.etlas.enums.CustomerType;
import com.etlas.mapper.MapperUtil;
import com.etlas.repository.CustomerRepository;
import com.etlas.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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

    @Override
    public CustomerDto saveNewCustomer(CustomerDto newCustomer) {
        CustomerDto customerToBeSave;
        if (newCustomer.isCompany()){
            customerToBeSave = adjustNewCustomerCompany(newCustomer);

        } else {
            customerToBeSave = adjustNewCustomerIndividual(newCustomer);
        }

        Customer savedCustomer = repository.save(mapper.convert(customerToBeSave,new Customer()));
        return mapper.convert(savedCustomer, new CustomerDto());
    }
    private CustomerDto adjustNewCustomerCompany(CustomerDto newCompany){
        newCompany.setCustomerType(CustomerType.COMPANY);
        newCompany.setFirstName(null);
        newCompany.setLastName(null);
        newCompany.setGender(null);
        return newCompany;
    }
    private CustomerDto adjustNewCustomerIndividual(CustomerDto newCompany){
        newCompany.setCustomerType(CustomerType.INDIVIDUAL);
        newCompany.setCompanyName(null);
        newCompany.setOfficeNumber(null);
        return newCompany;
    }

    @Override
    public CustomerDto deleteCustomer(long customerId) {
        Customer customerToBeDelete = repository.findById(customerId)
                .orElseThrow(NoSuchElementException::new);
        if (customerToBeDelete !=null){
            customerToBeDelete.setDeleted(true);
            repository.save(customerToBeDelete);
            return mapper.convert(customerToBeDelete, new CustomerDto());
        }
        return null;
    }
}
