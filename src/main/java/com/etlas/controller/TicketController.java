package com.etlas.controller;

import com.etlas.dto.CustomerDto;
import com.etlas.enums.CountriesTr;
import com.etlas.enums.CustomerType;
import com.etlas.enums.Gender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ticket")
public class TicketController {

    @GetMapping("/create")
    public String createTicket(Model model){
        model.addAttribute("newCustomer",new CustomerDto());
        model.addAttribute("countriesTr", CountriesTr.values());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("customerType", CustomerType.values());

        return "ticket/ticket-create";
    }
    @GetMapping("/list")
    public String getTicketList(Model model){

        return "ticket/list";
    }
}
