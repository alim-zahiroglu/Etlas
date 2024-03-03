package com.etlas.controller;

import com.etlas.dto.CustomerDto;
import com.etlas.enums.*;
import com.etlas.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/list")
    public String customerList(Model model){
        List<CustomerDto> customerList = customerService.getAllCustomers();
        model.addAttribute("customerList", customerList);
        return "/customer/customer-list";
    }
    @GetMapping("/create")
    public String customerCreate(Model model){
        model.addAttribute("newCustomer",new CustomerDto());
        model.addAttribute("countriesTr", CountriesTr.values());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("customerType", CustomerType.values());
        return "/customer/customer-create";
    }

    @PostMapping("/create")
    public String saveNewCustomer(@ModelAttribute("newCustomer") CustomerDto newCustomer, Model model){

        System.out.println(newCustomer);
//        model.addAttribute("newCustomer",new CustomerDto());
//        model.addAttribute("countriesTr", CountriesTr.values());
//        model.addAttribute("genders", Gender.values());
//        model.addAttribute("customerType", CustomerType.values());
        customerService.saveNewCustomer(newCustomer);
        return "redirect:/customer/list";
    }
}
