package com.etlas.controller;

import com.etlas.dto.CustomerDto;
import com.etlas.dto.TicketDto;
import com.etlas.enums.CountriesTr;
import com.etlas.enums.Gender;
import com.etlas.enums.currencyUnits;
import com.etlas.service.CustomerService;
import com.etlas.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ticket")
public class TicketController {
    private final UserService userService;
    private final CustomerService customerService;

    @GetMapping("/create")
    public String createTicket(Model model){
        model.addAttribute("newTicket",new TicketDto());
        model.addAttribute("moneyUnits", currencyUnits.values());
        model.addAttribute("countriesTr", CountriesTr.values());
        model.addAttribute("userList", userService.findAllUsers());
        model.addAttribute("customerList", customerService.getAllCustomers());
        model.addAttribute("currencyUnits", currencyUnits.values());
        model.addAttribute("newCustomer", new CustomerDto());
        model.addAttribute("genders", Gender.values());

        return "ticket/ticket-create";
    }

//    @PostMapping("/create")
//    public String saveTicket(@ModelAttribute("newTicket") TicketDto newTicket){
//        System.out.println(newTicket);
//
//        return "ticket/list";
//    }

//    @PostMapping("/create-add-customer")
//    public String addCustomer(@ModelAttribute("newCustomer") CustomerDto newCustomer,
//                              RedirectAttributes redirectAttributes){
//
//        CustomerDto createdCustomer = customerService.saveNewCustomer(newCustomer);
//        redirectAttributes.addFlashAttribute("customerIsCreated",true);
//        redirectAttributes.addFlashAttribute("createdCustomer",createdCustomer);
//
//        return "redirect:/ticket/create";
//    }

    @GetMapping("/list")
    public String getTicketList(Model model){

        return "ticket/list";
    }

    // Controller method for saving new ticket
    @PostMapping("/create")
    public ResponseEntity<String> saveNewTicket(@ModelAttribute TicketDto newTicket) {
        // Save new ticket logic
        return ResponseEntity.ok("Ticket saved successfully");
    }

    // Controller method for saving new customer
    @PostMapping("/create-add-customer")
    public String saveNewCustomer(@ModelAttribute("newTicket") TicketDto newTicket,
                                                  @ModelAttribute("newCustomer") CustomerDto newCustomer) {
        // Save new customer logic
        System.out.println(newCustomer + "*******************************");
        System.out.println(newTicket + "xxxxxxxxxxxxxxxxxxxx");

        return "redirect:/ticket/create";
    }



}
