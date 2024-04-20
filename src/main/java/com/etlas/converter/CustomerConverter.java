package com.etlas.converter;

import com.etlas.dto.CustomerDto;
import com.etlas.service.CustomerService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CustomerConverter implements Converter<String, CustomerDto> {
    private final CustomerService customerService;
    public CustomerConverter(CustomerService customerService) {
        this.customerService = customerService;
    }
    @Override
    public CustomerDto convert(String customerId) {
        if (customerId.equals("") || customerId.equals("0")) {
            return null;
        }
        long id = Long.parseLong(customerId);
        return customerService.findById(id);
    }
}
