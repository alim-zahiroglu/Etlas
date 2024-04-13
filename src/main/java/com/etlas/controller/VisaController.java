package com.etlas.controller;

import com.etlas.dto.CustomerDto;
import com.etlas.dto.VisaDto;
import com.etlas.enums.CountriesTr;
import com.etlas.enums.CurrencyUnits;
import com.etlas.enums.Gender;
import com.etlas.service.CardService;
import com.etlas.service.CustomerService;
import com.etlas.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/visa")
public class VisaController {
    private final CustomerService customerService;
    private final UserService userService;
    private final CardService cardService;
    @GetMapping("/create")
    public String visaCreate(Model model){
        VisaDto newVisa = new VisaDto();

        model.addAttribute("newVisa", newVisa);
        model.addAttribute("countriesTr", CountriesTr.values());
        model.addAttribute("customerList", customerService.getAllCustomers());
        model.addAttribute("userList", userService.findAllUsers());
        model.addAttribute("cardList", cardService.getAllCards());
        model.addAttribute("currencyUnits", CurrencyUnits.values());
        String currencySymbol = newVisa.getCurrencyUnit().getCurrencySymbol();
        model.addAttribute("currencySymbol", currencySymbol);

        model.addAttribute("newCustomer", new CustomerDto());
        model.addAttribute("genders", Gender.values());
        return "/visa/visa-create";
    }
}
