package com.etlas.controller;

import com.etlas.dto.CustomerDto;
import com.etlas.dto.UserDto;
import com.etlas.enums.*;
import com.etlas.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
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
        CustomerDto newCustomer = customerService.initializeNewCustomer();
        model.addAttribute("newCustomer",newCustomer);
        model.addAttribute("countriesTr", CountriesTr.values());
        model.addAttribute("genders", Gender.values());
        return "/customer/customer-create";
    }

    @PostMapping("/create")
    public String saveNewCustomer(@Valid @ModelAttribute("newCustomer") CustomerDto newCustomer,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){
        bindingResult = customerService.checkNewCustomerValidation(newCustomer,bindingResult);
        if (bindingResult.hasErrors()){

            model.addAttribute("countriesTr", CountriesTr.values());
            model.addAttribute("genders", Gender.values());
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
        boolean isCustomerDeletable = customerService.isCustomerDeletable(customerId);
        if (!isCustomerDeletable) {
            redirectAttributes.addFlashAttribute("customerIsDeleted", false);
            redirectAttributes.addFlashAttribute("deleteMessage", "because the customer is used in a ticket or has debt to pay.");
            return "redirect:/customer/list";
        }
        CustomerDto deletedCustomer = customerService.deleteCustomer(customerId);
        redirectAttributes.addFlashAttribute("customerIsDeleted",true);
        redirectAttributes.addFlashAttribute("deletedCustomer", deletedCustomer);
        return "redirect:/customer/list";
    }

    @GetMapping("/update/{customerId}")
    public String customerUpdate(@PathVariable("customerId") long customerId,
                                 @Param("from") String from, Model model){

        CustomerDto customerToBeUpdate = customerService.getCustomerById(customerId);

        model.addAttribute("customerToBeUpdate",customerToBeUpdate);
        model.addAttribute("countries",CountriesTr.values());
        model.addAttribute("from",from);

        if (customerToBeUpdate.getCustomerType().getDescription().equals("Company")){
            return "/customer/customer-update-company";
        } else {
            model.addAttribute("genders",Gender.values());
            return "/customer/customer-update-individual";
        }
    }

    @PostMapping("/update/{id}/{customerType}")
    public String saveUpdatedCustomer(@Valid @ModelAttribute("customerToBeUpdate") CustomerDto customerToBeUpdate,
                                      BindingResult bindingResult, @Param("from") String from,
                                      RedirectAttributes redirectAttributes, Model model){
        BindingResult newbindingResult = customerService.validateUpdateCustomer(customerToBeUpdate,bindingResult);

        model.addAttribute("countries",CountriesTr.values());
        model.addAttribute("from",from);

        if (newbindingResult.hasErrors()){
            if (customerToBeUpdate.getCustomerType().getDescription().equals("Company")){
                return "/customer/customer-update-company";
            }else {
                model.addAttribute("genders",Gender.values());
                return "/customer/customer-update-individual";
            }

        }

        CustomerDto updatedCustomer = customerService.saveUpdatedCustomer(customerToBeUpdate);
        redirectAttributes.addFlashAttribute("customerIsUpdated",true);
        redirectAttributes.addFlashAttribute("updatedCustomer",updatedCustomer);

        if (from.equals("visa")) return "redirect:/visa/list";
        if (from.equals("record")) return "redirect:/record/list";
        return "redirect:/customer/list";
    }

    @GetMapping("/details/{customerId}")
    public String showCustomerDetails(@PathVariable("customerId") long customerId,
                                      @Param("from") String from, Model model){
        CustomerDto customer = customerService.getCustomerById(customerId);
        model.addAttribute("customer",customer);
        model.addAttribute("from",from);
        if (customer.getCustomerType().getDescription().equals("Company")){
            return "/customer/customer-details-company";
        }
        return "/customer/customer-details-individual";
    }


}
