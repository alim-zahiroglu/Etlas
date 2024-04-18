package com.etlas.controller;

import com.etlas.dto.BalanceRecordDto;
import com.etlas.dto.CustomerDto;
import com.etlas.enums.CurrencyUnits;
import com.etlas.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/record")
public class BalanceController {
    private final CustomerService customerService;
    private final BalanceService balanceService;
    private final UserService userService;
    private final CardService cardService;
    private final TicketService ticketService;
    private final VisaService visaService;

    @GetMapping("/list")
    public String listBalance(Model model) {
        model.addAttribute("recordList", balanceService.getAllBalanceRecords());
        return "balance/balance-record-list";
    }
    @GetMapping("/create")
    public String createBalance(Model model) {
        BalanceRecordDto newBalance = balanceService.initiateNewBalance();
        model.addAttribute("newBalance", newBalance);
        model.addAttribute("customerList", customerService.getAllCustomers());
        model.addAttribute("userList", userService.findAllUsers());
        model.addAttribute("ticketList", ticketService.findAllTickets());
        model.addAttribute("visaList", visaService.getAllUniqueVisTypeWithCountry());
        model.addAttribute("currencyUnits", CurrencyUnits.values());
        model.addAttribute("cardList", cardService.getAllCards());
        model.addAttribute("currencySymbol", newBalance.getCurrencyUnit().getCurrencySymbol());
        return "balance/balance-create";
    }

    @PostMapping("/create")
    public String saveBalance(@Valid @ModelAttribute("newBalance") BalanceRecordDto newRecord,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes, Model model) {

        bindingResult = balanceService.validateBalanceRecord(newRecord, bindingResult);
        if (bindingResult.hasErrors()) {

            model.addAttribute("customerList", customerService.getAllCustomers());
            model.addAttribute("userList", userService.findAllUsers());
            model.addAttribute("ticketList", ticketService.findAllTickets());
            model.addAttribute("visaList", visaService.getAllVisas());
            model.addAttribute("currencyUnits", CurrencyUnits.values());
            model.addAttribute("cardList", cardService.getAllCards());
            model.addAttribute("currencySymbol", newRecord.getCurrencyUnit().getCurrencySymbol());

            return "balance/balance-create";
        }
        balanceService.saveBalanceRecord(newRecord);
        redirectAttributes.addFlashAttribute("isNewRecordSaved", true);
        return "redirect:/record/list";
    }

    @GetMapping("/delete")
    public String deleteBalance(@Param("recordId") String recordId, RedirectAttributes redirectAttributes) {
        balanceService.deleteBalanceRecord(Long.parseLong(recordId));
        redirectAttributes.addFlashAttribute("isRecordDeleted", true);
        return "redirect:/record/list";
    }

    @GetMapping("/update/{recordId}")
    public String updateBalance(@PathVariable long recordId, Model model) {

        BalanceRecordDto balanceRecord = balanceService.initiateUpdateRecord(recordId);
        model.addAttribute("balanceRecord", balanceRecord);
        model.addAttribute("customerList", customerService.getAllCustomers());
        model.addAttribute("userList", userService.findAllUsers());
        model.addAttribute("ticketList", ticketService.findAllTickets());
        model.addAttribute("visaList", visaService.getAllVisas());
        model.addAttribute("currencyUnits", CurrencyUnits.values());
        model.addAttribute("cardList", cardService.getAllCards());
        model.addAttribute("currencySymbol", balanceRecord.getCurrencyUnit().getCurrencySymbol());
        return "balance/balance-update";
    }
    @PostMapping("/update/{id}")
    public String saveUpdatedRecord(@Valid @ModelAttribute("balanceRecord") BalanceRecordDto updatedBalanceRecord,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes, Model model) {

        bindingResult = balanceService.validateBalanceRecord(updatedBalanceRecord, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("customerList", customerService.getAllCustomers());
            model.addAttribute("userList", userService.findAllUsers());
            model.addAttribute("ticketList", ticketService.findAllTickets());
            model.addAttribute("visaList", visaService.getAllVisas());
            model.addAttribute("currencyUnits", CurrencyUnits.values());
            model.addAttribute("cardList", cardService.getAllCards());
            model.addAttribute("currencySymbol", updatedBalanceRecord.getCurrencyUnit().getCurrencySymbol());
            return "balance/balance-update";
        }
        balanceService.saveUpdatedBalanceRecord(updatedBalanceRecord);
        redirectAttributes.addFlashAttribute("isRecordUpdated", true);
        return "redirect:/record/list";
    }

    @GetMapping("/customer-record/{customerId}")
    public String getCustomerBalance(@PathVariable long customerId, Model model) {
        CustomerDto giver = customerService.getCustomerById(customerId);
        BalanceRecordDto newBalance = balanceService.initiateNewBalance();

        newBalance.setGiver(giver); // set the giver to the record

        model.addAttribute("newBalance", newBalance);
        model.addAttribute("customerList", customerService.getAllCustomers());
        model.addAttribute("userList", userService.findAllUsers());
        model.addAttribute("ticketList", ticketService.findTicketsByCustomerId(customerId));
        model.addAttribute("visaList", visaService.getAllUniqueVisTypeWithCountryFromCustomer(customerId));
        model.addAttribute("currencyUnits", CurrencyUnits.values());
        model.addAttribute("cardList", cardService.getAllCards());
        model.addAttribute("currencySymbol", newBalance.getCurrencyUnit().getCurrencySymbol());
        return "balance/balance-customer-record";
    }

    @PostMapping("/customer-record/{customerId}")
    public String saveCustomerBalance(@Valid @ModelAttribute("newBalance") BalanceRecordDto newRecord,
                                      BindingResult bindingResult,  @PathVariable long customerId,
                                      RedirectAttributes redirectAttributes, Model model) {

        CustomerDto giver = customerService.getCustomerById(customerId);
        newRecord.setGiver(giver); // set the giver to the record
        bindingResult = balanceService.validateBalanceRecord(newRecord, bindingResult);
        if (bindingResult.hasErrors()) {

            model.addAttribute("customerList", customerService.getAllCustomers());
            model.addAttribute("userList", userService.findAllUsers());
            model.addAttribute("ticketList", ticketService.findTicketsByCustomerId(customerId));
            model.addAttribute("visaList", visaService.getAllUniqueVisTypeWithCountryFromCustomer(customerId));
            model.addAttribute("currencyUnits", CurrencyUnits.values());
            model.addAttribute("cardList", cardService.getAllCards());
            model.addAttribute("currencySymbol", newRecord.getCurrencyUnit().getCurrencySymbol());

            return "balance/balance-customer-record";
        }
        balanceService.saveBalanceRecord(newRecord);
        redirectAttributes.addFlashAttribute("isNewRecordSaved", true);
        return "redirect:/customer/list";
    }

}
