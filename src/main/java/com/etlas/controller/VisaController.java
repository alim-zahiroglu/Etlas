package com.etlas.controller;

import com.etlas.dto.CustomerDto;
import com.etlas.dto.VisaDto;
import com.etlas.enums.CountriesTr;
import com.etlas.enums.CurrencyUnits;
import com.etlas.enums.Gender;
import com.etlas.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/visa")
public class VisaController {
    private final CustomerService customerService;
    private final UserService userService;
    private final CardService cardService;
    private final VisaService visaService;
    private final VisaTypeService visaTypeService;

    private boolean isNewCustomerAdded = false;
    private String addedCustomerId;
    @GetMapping("/create")
    public String visaCreate(Model model){
        // check new customer added or not
        if (!isNewCustomerAdded){
            VisaDto newVisa = visaService.initializeVisa();
            model.addAttribute("newVisa", newVisa);
            String currencySymbol = newVisa.getCurrencyUnit().getCurrencySymbol();
            model.addAttribute("currencySymbol", currencySymbol);
        }
        CustomerDto newCustomer = customerService.initializeNewCustomer();

        model.addAttribute("newCustomer", newCustomer);
        model.addAttribute("genders", Gender.values());

        model.addAttribute("countriesTr", CountriesTr.values());
        model.addAttribute("visaTypes", visaTypeService.getAllVisaTypes());
        model.addAttribute("customerList", customerService.getAllCustomers());
        model.addAttribute("userList", userService.findAllUsers());
        model.addAttribute("cardList", cardService.getAllCards());
        model.addAttribute("currencyUnits", CurrencyUnits.values());

        isNewCustomerAdded = false; // set new customer added false
        return "/visa/visa-create";
    }
    @PostMapping("/create")
    public String saveNewVisa(@Valid @ModelAttribute("newVisa") VisaDto newVisa,
                              BindingResult bindingResult, Model model,
                              RedirectAttributes redirectAttributes) {
        // check which button is clicked
        if (isNewCustomerAdded) {
            CustomerDto addedCustomer = customerService.findById(Long.parseLong(addedCustomerId));
            VisaDto visa = visaService.adjustNewVisa(newVisa,addedCustomerId);
            String currencySymbol = visa.getCurrencyUnit().getCurrencySymbol();

            redirectAttributes.addFlashAttribute("newVisa", visa);
            redirectAttributes.addFlashAttribute("addedCustomer",addedCustomer);
            redirectAttributes.addFlashAttribute("isNewCustomerAdded",true);
            redirectAttributes.addFlashAttribute("currencySymbol",currencySymbol);

            return "redirect:/visa/create";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("countriesTr", CountriesTr.values());
            model.addAttribute("visaTypes", visaTypeService.getAllVisaTypes());
            model.addAttribute("customerList", customerService.getAllCustomers());
            model.addAttribute("userList", userService.findAllUsers());
            model.addAttribute("cardList", cardService.getAllCards());
            model.addAttribute("currencyUnits", CurrencyUnits.values());

            CustomerDto newCustomer = customerService.initializeNewCustomer();

            model.addAttribute("newCustomer", newCustomer);
            model.addAttribute("genders", Gender.values());

            return "/visa/visa-create";
        }

        VisaDto savedVisa = visaService.saveNewVisa(newVisa);
        redirectAttributes.addFlashAttribute("isNewVisaSaved",true);
        return "redirect:/ticket/create";
    }


    // Controller method for saving new customer
    @PostMapping("/create-add-customer")
    public ResponseEntity<String> addNewCustomer(@ModelAttribute("newCustomer") CustomerDto newCustomer) {
        // Save new customer logic
        CustomerDto savedCustomer = customerService.saveNewCustomer(newCustomer);

        isNewCustomerAdded = true;
        addedCustomerId = String.valueOf(savedCustomer.getId());

        return ResponseEntity.ok("new customer added");

    }
}
