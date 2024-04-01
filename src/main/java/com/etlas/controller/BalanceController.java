package com.etlas.controller;

import com.etlas.dto.BalanceRecordDto;
import com.etlas.dto.CardDto;
import com.etlas.enums.CurrencyUnits;
import com.etlas.service.BalanceService;
import com.etlas.service.CardService;
import com.etlas.service.CustomerService;
import com.etlas.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
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
@RequestMapping("/record")
public class BalanceController {
    private final CustomerService customerService;
    private final BalanceService balanceService;
    private final UserService userService;
    private final CardService cardService;

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
        model.addAttribute("currencyUnits", CurrencyUnits.values());
        model.addAttribute("cardList", cardService.getAllCards());
        model.addAttribute("currencySymbol", newBalance.getCurrencyUnit().getCurrencySymbol());
        return "balance/balance-create";
    }

    @PostMapping("/create")
    public String saveBalance(@Valid @ModelAttribute("newBalance") BalanceRecordDto newRecord,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("customerList", customerService.getAllCustomers());
            model.addAttribute("userList", userService.findAllUsers());
            model.addAttribute("currencyUnits", CurrencyUnits.values());
            model.addAttribute("cardList", cardService.getAllCards());
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

}
