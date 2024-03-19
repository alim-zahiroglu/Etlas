package com.etlas.controller;

import com.etlas.dto.CustomerDto;
import com.etlas.dto.TicketDto;
import com.etlas.enums.CountriesTr;
import com.etlas.enums.Gender;
import com.etlas.enums.CurrencyUnits;
import com.etlas.service.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ticket")
public class TicketController {
    private final TicketService ticketService;
    private final UserService userService;
    private final CustomerService customerService;
    private final AirLineService airLineService;
    private final AirportService airportService;


    private boolean isNewCustomerAdded = false;
    private String addedCustomerId;

    @GetMapping("/create")
    public String createTicket(Model model){
        // check new customer added or not
        if (!isNewCustomerAdded){
            TicketDto newTicket = ticketService.initializeNewTicket();
            model.addAttribute("newTicket",newTicket);
        }

        model.addAttribute("newCustomer", new CustomerDto());
        model.addAttribute("airLines", airLineService.getAllAirLines());
        model.addAttribute("airports", airportService.getAllAirports());
        model.addAttribute("countriesTr", CountriesTr.values());
        model.addAttribute("userList", userService.findAllUsers());
        model.addAttribute("customerList", customerService.getAllCustomers());
        model.addAttribute("currencyUnits", CurrencyUnits.values());
        model.addAttribute("genders", Gender.values());

        isNewCustomerAdded = false; // set new customer added false
        return "ticket/ticket-create";
    }

    @PostMapping("/create")
    public String saveNewTicket(@ModelAttribute("newTicket") TicketDto newTicket,
                                RedirectAttributes redirectAttributes, Model model) {

        // check which button is clicked
        if (isNewCustomerAdded) {
            CustomerDto addedCustomer = customerService.findById(Long.parseLong(addedCustomerId));
            TicketDto ticket = ticketService.adjustNewTicket(newTicket,addedCustomerId);
            redirectAttributes.addFlashAttribute("newTicket",ticket);
            redirectAttributes.addFlashAttribute("addedCustomer",addedCustomer);
            redirectAttributes.addFlashAttribute("isNewCustomerAdded",true);
            return "redirect:/ticket/create";
        }
        System.out.println("************************************************************************************");
        System.out.println(newTicket);
        ticketService.saveNewTicket(newTicket);

        redirectAttributes.addFlashAttribute("savedTicket",new TicketDto());
        redirectAttributes.addFlashAttribute("isNewTicketSaved",true);
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
