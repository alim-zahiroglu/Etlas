package com.etlas.controller;

import com.etlas.dto.TicketDto;
import com.etlas.enums.CountriesTr;
import com.etlas.enums.currencyUnits;
import com.etlas.service.CustomerService;
import com.etlas.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

        return "ticket/ticket-create";
    }

    @PostMapping("/create")
    public String saveTicket(@ModelAttribute("newTicket") TicketDto newTicket){
        System.out.println(newTicket);

        return "ticket/list";
    }
    @GetMapping("/list")
    public String getTicketList(Model model){

        return "ticket/list";
    }
}
