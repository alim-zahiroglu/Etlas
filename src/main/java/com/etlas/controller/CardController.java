package com.etlas.controller;

import com.etlas.dto.CardDto;
import com.etlas.dto.UserDto;
import com.etlas.enums.Gender;
import com.etlas.enums.Role;
import com.etlas.enums.UserStatus;
import com.etlas.service.BankService;
import com.etlas.service.CardService;
import com.etlas.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/card")
public class CardController {
    private final BankService bankService;
    private final CardService cardService;

    @GetMapping("/list")
    public String getAllUsers(Model model){
        return "user/user-list";
    }

    @GetMapping("/create")
    public String userCreate(Model model){
        model.addAttribute("newCard",new CardDto());
        model.addAttribute("bankNames", bankService.getAllBankNames());
        return "/card/card-create";
    }
    @PostMapping("/create")
    public String saveUser(@Valid @ModelAttribute("newCard") CardDto newCard, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes, Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("bankNames", bankService.getAllBankNames());
            return "card/card-create";
        }
        CardDto createdCard = cardService.saveNewCard(newCard);
        System.out.println("createdCard = " + createdCard);
        redirectAttributes.addFlashAttribute("IsNewCardSaved", true);
        redirectAttributes.addFlashAttribute("savedCardName", createdCard.getCardOwner());
        return "redirect:/card/list";
    }

//    @GetMapping("/delete")
//    public String deleteUser(@RequestParam("username") String username,
//                             RedirectAttributes redirectAttributes){
//        if (bankService.isUserDeletable(username)) {
//            UserDto deletedUser = bankService.deleteUser(username);
//            redirectAttributes.addFlashAttribute("userIsDeleted", true);
//            redirectAttributes.addFlashAttribute("deletedUser", deletedUser);
//            return "redirect:/user/list";
//        }
//        redirectAttributes.addFlashAttribute("userIsDeleted", false);
//        redirectAttributes.addFlashAttribute("deleteMessage", "This is the only admin user or the user used in a ticket, transaction or visa");
//        return "redirect:/user/list";
//    }
//
//    @GetMapping("/update/{userName}")
//    public String updateUser(@PathVariable("userName") String userName, Model model){
//        UserDto userToBeUpdate = bankService.getUserByUserName(userName);
//        model.addAttribute("userToBeUpdate",userToBeUpdate);
//        model.addAttribute("roles", Role.values());
//        model.addAttribute("genders", Gender.values());
//        model.addAttribute("userStatuses", UserStatus.values());
//        return "user/user-update";
//    }
//
//    @PostMapping("/update/{id}")
//    public String saveUpdatedUser(@Valid @ModelAttribute("userToBeUpdate") UserDto userToBeUpdate, BindingResult bindingResult,
//                                  Model model, RedirectAttributes redirectAttributes){
//        bindingResult = bankService.validateUpdatedUser(userToBeUpdate,bindingResult);
//        if (bindingResult.hasErrors()){
//            userToBeUpdate.setUseDefaultPassword(false);
//            userToBeUpdate.setUseCurrentPassword(false);
//            model.addAttribute("roles", Role.values());
//            model.addAttribute("genders", Gender.values());
//            model.addAttribute("userStatuses", UserStatus.values());
//            return "user/user-update";
//        }
//        UserDto updatedUser = bankService.saveUpdatedUser(userToBeUpdate);
//        redirectAttributes.addFlashAttribute("userIsUpdated", true);
//        redirectAttributes.addFlashAttribute("updatedUser", updatedUser);
//        return "redirect:/user/list";
//    }
}
