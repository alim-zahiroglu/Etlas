package com.etlas.controller;

import com.etlas.dto.CustomerDto;
import com.etlas.dto.UserDto;
import com.etlas.enums.*;
import com.etlas.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String saveNewCustomer(@Valid @ModelAttribute("newCustomer") CustomerDto newCustomer,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){
        bindingResult = customerService.checkNewCustomerValidation(newCustomer,bindingResult);
        if (bindingResult.hasErrors()){

            model.addAttribute("countriesTr", CountriesTr.values());
            model.addAttribute("genders", Gender.values());
            model.addAttribute("customerType", CustomerType.values());
            return "/customer/customer-create";
        }

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
        return "redirect:/customer/list";
    }

    @GetMapping("/update/{customerId}")
    public String customerUpdate(@PathVariable("customerId") long customerId, Model model){

        CustomerDto customerToBeUpdate = customerService.getCustomerById(customerId);

        model.addAttribute("customerToBeUpdate",customerToBeUpdate);
        model.addAttribute("countries",CountriesTr.values());

        if (customerToBeUpdate.getCustomerType().getDescription().equals("Company")){
            return "/customer/customer-update-company";
        } else {
            model.addAttribute("genders",Gender.values());
            return "/customer/customer-update-individual";
        }
    }

    @PostMapping("/update/{id}/{customerType}")
    public String saveUpdatedCustomer(@Valid @ModelAttribute("customerToBeUpdate") CustomerDto customerToBeUpdate,
                                      BindingResult bindingResult,RedirectAttributes redirectAttributes, Model model){
        bindingResult = customerService.validateUpdateCustomer(customerToBeUpdate,bindingResult);
        if (bindingResult.hasErrors()){
            if (customerToBeUpdate.getCustomerType().getDescription().equals("Company")){
                model.addAttribute("countries",CountriesTr.values());
                return "/customer/customer-update-company";
            }else {
                model.addAttribute("countries",CountriesTr.values());
                model.addAttribute("genders",Gender.values());
                return "/customer/customer-update-individual";
            }

        }

        CustomerDto updatedCustomer = customerService.saveUpdatedCustomer(customerToBeUpdate);
        redirectAttributes.addFlashAttribute("customerIsUpdated",true);
        redirectAttributes.addFlashAttribute("updatedCustomer",updatedCustomer);
        return "redirect:/customer/list";
    }


}
