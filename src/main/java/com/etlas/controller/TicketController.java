package com.etlas.controller;

import com.etlas.dto.CustomerDto;
import com.etlas.dto.TicketDto;
import com.etlas.enums.CountriesTr;
import com.etlas.enums.Gender;
import com.etlas.enums.CurrencyUnits;
import com.etlas.enums.PaidType;
import com.etlas.service.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
    private final CardService cardService;

    private boolean isNewCustomerAdded = false;
    private String addedCustomerId;

    @GetMapping("/list")
    public String getAllTickets(Model model){
        List<TicketDto> ticketList = ticketService.findAllTickets();
        model.addAttribute("ticketList",ticketList);
        return "/ticket/ticket-list";
    }
    @GetMapping("/create")
    public String createTicket(Model model){
        // check new customer added or not
        if (!isNewCustomerAdded){
            TicketDto newTicket = ticketService.initializeNewTicket();
            model.addAttribute("newTicket",newTicket);
            String currencySymbol = newTicket.getCurrencyUnit().getCurrencySymbol();
            model.addAttribute("currencySymbol", currencySymbol);
        }
        model.addAttribute("newCustomer", new CustomerDto());
        model.addAttribute("airLines", airLineService.getAllAirLines());
        model.addAttribute("airports", airportService.getAllAirports());
        model.addAttribute("countriesTr", CountriesTr.values());
        model.addAttribute("userList", userService.findAllUsers());
        model.addAttribute("customerList", customerService.getAllCustomers());
        model.addAttribute("paidTypes", PaidType.values());
        model.addAttribute("currencyUnits", CurrencyUnits.values());
        model.addAttribute("cardList", cardService.findAllCardList());
        model.addAttribute("genders", Gender.values());


        isNewCustomerAdded = false; // set new customer added false
        return "ticket/ticket-create";
    }

    @PostMapping("/create")
    public String saveNewTicket(@Valid @ModelAttribute("newTicket") TicketDto newTicket,
                                BindingResult bindingResult, Model model,
                                RedirectAttributes redirectAttributes) {
        // check which button is clicked
        if (isNewCustomerAdded) {
            CustomerDto addedCustomer = customerService.findById(Long.parseLong(addedCustomerId));
            TicketDto ticket = ticketService.adjustNewTicket(newTicket,addedCustomerId);
            String currencySymbol = newTicket.getCurrencyUnit().getCurrencySymbol();

            redirectAttributes.addFlashAttribute("newTicket",ticket);
            redirectAttributes.addFlashAttribute("addedCustomer",addedCustomer);
            redirectAttributes.addFlashAttribute("isNewCustomerAdded",true);
            redirectAttributes.addFlashAttribute("currencySymbol",currencySymbol);

            return "redirect:/ticket/create";
        }
        bindingResult = ticketService.validateTicket(newTicket,bindingResult);
        if (bindingResult.hasErrors()){
            String currencySymbol = newTicket.getCurrencyUnit().getCurrencySymbol();
            model.addAttribute("newCustomer", new CustomerDto());
            model.addAttribute("airLines", airLineService.getAllAirLines());
            model.addAttribute("airports", airportService.getAllAirports());
            model.addAttribute("countriesTr", CountriesTr.values());
            model.addAttribute("userList", userService.findAllUsers());
            model.addAttribute("customerList", customerService.getAllCustomers());
            model.addAttribute("currencyUnits", CurrencyUnits.values());
            model.addAttribute("paidTypes", PaidType.values());
            model.addAttribute("cardList", cardService.findAllCardList());
            model.addAttribute("genders", Gender.values());
            model.addAttribute("currencySymbol", currencySymbol);

            System.out.println(bindingResult.getAllErrors() + "**************************************");

            System.out.println(newTicket + "**************************************");

            return "ticket/ticket-create";
        }

        TicketDto savedTicket = ticketService.saveNewTicket(newTicket);
        redirectAttributes.addFlashAttribute("isNewTicketSaved",true);
        redirectAttributes.addFlashAttribute("savedTicketName",savedTicket.getPnrNo());

        System.out.println(newTicket + "**************************************");
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

    @GetMapping("/update/{ticketId}")
    public String updateTicket(@PathVariable long ticketId, Model model){

        if (!isNewCustomerAdded){
            TicketDto ticketTobeUpdate = ticketService.findById(ticketId);
            TicketDto ticket = ticketService.prepareTicketToUpdate(ticketTobeUpdate);
            model.addAttribute("ticketTobeUpdate", ticket);

            String currencySymbol = ticket.getCurrencyUnit().getCurrencySymbol();
            model.addAttribute("currencySymbol", currencySymbol);
        }

        model.addAttribute("newCustomer", new CustomerDto());
        model.addAttribute("airLines", airLineService.getAllAirLines());
        model.addAttribute("airports", airportService.getAllAirports());
        model.addAttribute("userList", userService.findAllUsers());
        model.addAttribute("customerList", customerService.getAllCustomers());
        model.addAttribute("countriesTr", CountriesTr.values());
        model.addAttribute("currencyUnits", CurrencyUnits.values());
        model.addAttribute("paidTypes", PaidType.values());
        model.addAttribute("cardList", cardService.findAllCardList());
        model.addAttribute("genders", Gender.values());

        isNewCustomerAdded = false;
        return "ticket/ticket-update";
    }


    @PostMapping("/update/{id}")
    public String saveUpdatedTicket(@Valid @ModelAttribute("ticketTobeUpdate") TicketDto updatedTicket,
                                    BindingResult bindingResult, Model model,
                                    RedirectAttributes redirectAttributes){

        if (isNewCustomerAdded) {
            CustomerDto addedCustomer = customerService.findById(Long.parseLong(addedCustomerId));
            TicketDto ticket = ticketService.adjustNewTicket(updatedTicket, addedCustomerId);
            String currencySymbol = updatedTicket.getCurrencyUnit().getCurrencySymbol();

            redirectAttributes.addFlashAttribute("ticketTobeUpdate", ticket);
            redirectAttributes.addFlashAttribute("addedCustomer", addedCustomer);
            redirectAttributes.addFlashAttribute("isNewCustomerAdded", true);
            redirectAttributes.addFlashAttribute("currencySymbol", currencySymbol);

            return "redirect:/ticket/update/{id}";
        }


        ticketService.validateUpdatedTicket(updatedTicket, bindingResult);
        if (bindingResult.hasErrors()){
            String currencySymbol = updatedTicket.getCurrencyUnit().getCurrencySymbol();

            model.addAttribute("newCustomer", new CustomerDto());
            model.addAttribute("airLines", airLineService.getAllAirLines());
            model.addAttribute("airports", airportService.getAllAirports());
            model.addAttribute("countriesTr", CountriesTr.values());
            model.addAttribute("userList", userService.findAllUsers());
            model.addAttribute("customerList", customerService.getAllCustomers());
            model.addAttribute("currencyUnits", CurrencyUnits.values());
            model.addAttribute("paidTypes", PaidType.values());
            model.addAttribute("cardList", cardService.findAllCardList());
            model.addAttribute("genders", Gender.values());
            model.addAttribute("currencySymbol", currencySymbol);

            return "ticket/ticket-update";
        }

        TicketDto savedTicket = ticketService.saveUpdatedTicket(updatedTicket);

        redirectAttributes.addFlashAttribute("savedTicket",savedTicket.getPnrNo());
        redirectAttributes.addFlashAttribute("isTicketUpdated",true);
        redirectAttributes.addFlashAttribute("updatedTicket",savedTicket);

        return "redirect:/ticket/list";
    }

    @GetMapping("/delete")
    public String deleteTicket(@RequestParam("ticketId") long ticketId, RedirectAttributes redirectAttributes) {
        String deletedTicketName = ticketService.findById(ticketId).getPnrNo();
        if (ticketService.isTicketDeletable(ticketId)) {
            boolean ticketIsDeleted =ticketService.deleteTicket(ticketId);
            redirectAttributes.addFlashAttribute("ticketIsDeleted", ticketIsDeleted);
            redirectAttributes.addFlashAttribute("deletedTicketName", deletedTicketName);
            return "redirect:/ticket/list";
        }

        redirectAttributes.addFlashAttribute("ticketIsDeleted", false);
        redirectAttributes.addFlashAttribute("deleteMessage", "because the ticket didn't used in any flight yet.");
        return "redirect:/ticket/list";
    }


    // customer details
    @GetMapping("/customer/details/{customerId}")
    public String showCustomerDetails(@PathVariable("customerId") long customerId, Model model){
        CustomerDto customer = customerService.getCustomerById(customerId);
        model.addAttribute("customer",customer);
        if (customer.getCustomerType().getDescription().equals("Company")){
            return "/ticket/customer-details-company";
        }
        return "/ticket/customer-details-individual";
    }

    @GetMapping("/customer/update/{customerId}")
    public String customerUpdate(@PathVariable("customerId") long customerId, Model model){

        CustomerDto customerToBeUpdate = customerService.getCustomerById(customerId);

        model.addAttribute("customerToBeUpdate",customerToBeUpdate);
        model.addAttribute("countries",CountriesTr.values());

        if (customerToBeUpdate.getCustomerType().getDescription().equals("Company")){
            return "/ticket/customer-update-company";
        } else {
            model.addAttribute("genders",Gender.values());
            return "/ticket/customer-update-individual";
        }
    }

    @PostMapping("/customer/update/{id}/{customerType}")
    public String saveUpdatedCustomer(@Valid @ModelAttribute("customerToBeUpdate") CustomerDto customerToBeUpdate,
                                      BindingResult bindingResult,RedirectAttributes redirectAttributes, Model model){
        bindingResult = customerService.validateUpdateCustomer(customerToBeUpdate,bindingResult);
        if (bindingResult.hasErrors()){
            if (customerToBeUpdate.getCustomerType().getDescription().equals("Company")){
                model.addAttribute("countries",CountriesTr.values());
                return "/ticket/customer-update-company";
            }else {
                model.addAttribute("countries",CountriesTr.values());
                model.addAttribute("genders",Gender.values());
                return "/ticket/customer-update-individual";
            }

        }

        CustomerDto updatedCustomer = customerService.saveUpdatedCustomer(customerToBeUpdate);
        redirectAttributes.addFlashAttribute("customerIsUpdated",true);
        redirectAttributes.addFlashAttribute("updatedCustomerName",updatedCustomer.getFirstName() + " " + updatedCustomer.getLastName());
        return "redirect:/ticket/list";
    }


}
