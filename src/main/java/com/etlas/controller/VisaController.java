package com.etlas.controller;

import com.etlas.dto.CustomerDto;
import com.etlas.dto.VisaDto;
import com.etlas.enums.CountriesTr;
import com.etlas.enums.CurrencyUnits;
import com.etlas.enums.Gender;
import com.etlas.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/list")
    public String visaList(Model model){
        model.addAttribute("visaList", visaService.getAllVisas());
        return "/visa/visa-list";
    }

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
            // save new customer if new customer added
            customerService.saveNewCustomerIfAdded(Long.parseLong(newVisa.getCustomerUI()));

            String currencySymbol = newVisa.getCurrencyUnit().getCurrencySymbol();

            model.addAttribute("countriesTr", CountriesTr.values());
            model.addAttribute("visaTypes", visaTypeService.getAllVisaTypes());
            model.addAttribute("customerList", customerService.getAllCustomers());
            model.addAttribute("userList", userService.findAllUsers());
            model.addAttribute("cardList", cardService.getAllCards());
            model.addAttribute("currencyUnits", CurrencyUnits.values());
            model.addAttribute("currencySymbol", currencySymbol);

            CustomerDto newCustomer = customerService.initializeNewCustomer();

            model.addAttribute("newCustomer", newCustomer);
            model.addAttribute("genders", Gender.values());

            return "/visa/visa-create";
        }

        visaService.saveNewVisa(newVisa);
        redirectAttributes.addFlashAttribute("isNewVisaSaved",true);
        return "redirect:/visa/create";
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

    @GetMapping("/update/{visaId}")
    public String visaUpdate(@PathVariable long visaId, Model model) {

        if (!isNewCustomerAdded){
            VisaDto visa = visaService.findById(visaId);
            VisaDto visaToBeUpdate = visaService.prepareVisaToUpdate(visa);
            model.addAttribute("visaToBeUpdate", visaToBeUpdate);

            String currencySymbol = visa.getCurrencyUnit().getCurrencySymbol();
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
        return "/visa/visa-update";
    }
    @PostMapping("/update/{id}")
    public String saveUpdatedVisa(@Valid @ModelAttribute("visaToBeUpdate") VisaDto visaToBeUpdate,
                                  BindingResult bindingResult, Model model,
                                  RedirectAttributes redirectAttributes) {
        // check which button is clicked
        if (isNewCustomerAdded) {
            CustomerDto addedCustomer = customerService.findById(Long.parseLong(addedCustomerId));
            VisaDto visa = visaService.adjustNewVisa(visaToBeUpdate, addedCustomerId);
            String currencySymbol = visa.getCurrencyUnit().getCurrencySymbol();

            redirectAttributes.addFlashAttribute("visaToBeUpdate", visa);
            redirectAttributes.addFlashAttribute("addedCustomer", addedCustomer);
            redirectAttributes.addFlashAttribute("isNewCustomerAdded", true);
            redirectAttributes.addFlashAttribute("currencySymbol", currencySymbol);

            return "redirect:/visa/update/" + visaToBeUpdate.getId();
        }
        if (bindingResult.hasErrors()) {
            // save new customer if new customer added
            customerService.saveNewCustomerIfAdded(Long.parseLong(visaToBeUpdate.getCustomerUI()));

            String currencySymbol = visaToBeUpdate.getCurrencyUnit().getCurrencySymbol();

            model.addAttribute("countriesTr", CountriesTr.values());
            model.addAttribute("visaTypes", visaTypeService.getAllVisaTypes());
            model.addAttribute("customerList", customerService.getAllCustomers());
            model.addAttribute("userList", userService.findAllUsers());
            model.addAttribute("cardList", cardService.getAllCards());
            model.addAttribute("currencyUnits", CurrencyUnits.values());
            model.addAttribute("currencySymbol", currencySymbol);

            CustomerDto newCustomer = customerService.initializeNewCustomer();

            model.addAttribute("newCustomer", newCustomer);

            return "/visa/visa-update";

        }
        visaService.saveUpdatedVisa(visaToBeUpdate);
        redirectAttributes.addFlashAttribute("isVisaUpdated", true);
        return "redirect:/visa/list";
    }

    @GetMapping("/delete")
    public String deleteVisa(@Param("visaId") long visaId, RedirectAttributes redirectAttributes) {
        visaService.deleteVisa(visaId);
        redirectAttributes.addFlashAttribute("visaIsDeleted", true);
        return "redirect:/visa/list";
    }
}
