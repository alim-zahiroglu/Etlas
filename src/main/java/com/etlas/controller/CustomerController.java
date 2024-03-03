package com.etlas.controller;

import com.etlas.dto.CustomerDto;
import com.etlas.dto.UserDto;
import com.etlas.enums.*;
import com.etlas.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String saveNewCustomer(@ModelAttribute("newCustomer") CustomerDto newCustomer,
                                  RedirectAttributes redirectAttributes, Model model){

//        model.addAttribute("newCustomer",new CustomerDto());
//        model.addAttribute("countriesTr", CountriesTr.values());
//        model.addAttribute("genders", Gender.values());
//        model.addAttribute("customerType", CustomerType.values());

        CustomerDto createdCustomer = customerService.saveNewCustomer(newCustomer);
        redirectAttributes.addFlashAttribute("customerIsCreated",true);
        redirectAttributes.addFlashAttribute("createdCustomer",createdCustomer);

        return "redirect:/customer/list";
    }

    @GetMapping("/delete")
    public String deleteCustomer(@RequestParam("customerId") long customerId,
                                 RedirectAttributes redirectAttributes){
        CustomerDto deletedCustomer = customerService.deleteCustomer(customerId);
        redirectAttributes.addFlashAttribute("customerIsDeleted",true);
        redirectAttributes.addFlashAttribute("deletedCustomer", deletedCustomer);
        System.out.println(deletedCustomer);
        return "redirect:/customer/list";
    }
}
