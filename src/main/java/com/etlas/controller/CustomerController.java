package com.etlas.controller;

import com.etlas.dto.CustomerDto;
import com.etlas.enums.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @GetMapping("/create")
    public String customerCreate(Model model){
        model.addAttribute("newCustomer",new CustomerDto());
        model.addAttribute("countriesTr", CountriesTr.values());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("customerType", CustomerType.values());
        return "/customer/customer-create";
    }
}
