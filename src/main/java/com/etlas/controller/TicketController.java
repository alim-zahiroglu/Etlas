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
    private boolean isNewCustomerAdded = false;

    @GetMapping("/create")
    public String createTicket(Model model){

        if (!isNewCustomerAdded){
            model.addAttribute("newTicket",new TicketDto());
        }

        model.addAttribute("countriesTr", CountriesTr.values());
        model.addAttribute("userList", userService.findAllUsers());
        model.addAttribute("customerList", customerService.getAllCustomers());
        model.addAttribute("passengerList", customerService.getAllIndividualCustomers());
        model.addAttribute("currencyUnits", currencyUnits.values());
        model.addAttribute("newCustomer", new CustomerDto());
        model.addAttribute("genders", Gender.values());
        isNewCustomerAdded = false;
        return "ticket/ticket-create";
    }



    @GetMapping("/list")
    public String getTicketList(Model model){

        return "ticket/list";
    }

    // Controller method for saving new ticket
    @PostMapping("/create")
    public String saveNewTicket(@ModelAttribute("newTicket") TicketDto newTicket,
                                RedirectAttributes redirectAttributes,Model model) {
        // Save new ticket logic
        if (isNewCustomerAdded) {
            System.out.println(newTicket + "xxxxxxxxxxxxxxxxxxxx");

            System.out.println("********************"+newTicket.getPassengers());
            System.out.println("********************"+newTicket.getPayedCustomer());

            redirectAttributes.addFlashAttribute("newTicket",newTicket);

//            model.addAttribute("newTicket",newTicket);
//            model.addAttribute("countriesTr", CountriesTr.values());
//            model.addAttribute("userList", userService.findAllUsers());
//            model.addAttribute("customerList", customerService.getAllCustomers());
//            model.addAttribute("passengerList", customerService.getAllIndividualCustomers());
//            model.addAttribute("currencyUnits", currencyUnits.values());
//            model.addAttribute("newCustomer", new CustomerDto());
//            model.addAttribute("genders", Gender.values());
//            isNewCustomerAdded = false;
            return "redirect:/ticket/create";

        }

        return "redirect:/ticket/create";
    }

    // Controller method for saving new customer
    @PostMapping("/create-add-customer")
    public ResponseEntity<String> addNewCustomer(@ModelAttribute("newTicket") TicketDto ticketDto,
                                 @ModelAttribute("newCustomer") CustomerDto newCustomer,
                                 RedirectAttributes redirectAttributes, Model model) {
        // Save new customer logic
        System.out.println(newCustomer + "*******************************");
        System.out.println(ticketDto + "xxxxxxxxxxxxxxxxxxxx");
//        ticketDto.setPayedCustomer(newCustomer);
        isNewCustomerAdded = true;
        return ResponseEntity.ok("new customer added");

//        redirectAttributes.addFlashAttribute("newTicket",ticketDto);
//        model.addAttribute("newTicket",ticketDto);

//        return "redirect:/ticket/create";
//        return "/ticket/list";
    }



}
