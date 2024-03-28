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
    public String getAllCards(Model model){
        model.addAttribute("cardList", cardService.getAllCards());
        return "card/card-list";
    }

    @GetMapping("/list/card")
    public String getAllCardsCardView(Model model){
        model.addAttribute("cardList", cardService.getAllCards());
        return "card/card-list-card";
    }

    @GetMapping("/create")
    public String cardCreate(Model model){
        CardDto newCard = cardService.initiateNewCard();
        model.addAttribute("newCard",newCard);
        model.addAttribute("bankNames", bankService.getAllBankNames());
        return "/card/card-create";
    }
    @PostMapping("/create")
    public String saveCard(@Valid @ModelAttribute("newCard") CardDto newCard, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes, Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("bankNames", bankService.getAllBankNames());
            return "card/card-create";
        }
        CardDto createdCard = cardService.saveNewCard(newCard);
        redirectAttributes.addFlashAttribute("isNewCardSaved", true);
        redirectAttributes.addFlashAttribute("savedCardName", createdCard.getCardOwner());
        return "redirect:/card/list";
    }

    @GetMapping("/delete")
    public String deleteCard(@RequestParam("cardId") String cardId,
                             RedirectAttributes redirectAttributes){
        if (cardService.isCardDeletable(cardId)) {
            CardDto deletedCard = cardService.deleteCard(cardId);
            redirectAttributes.addFlashAttribute("isCardDeleted", true);
            redirectAttributes.addFlashAttribute("deleteCardName", deletedCard.getCardOwner());
            return "redirect:/card/list";
        }
        redirectAttributes.addFlashAttribute("isCardDeleted", false);
        redirectAttributes.addFlashAttribute("deleteMessage", "Because this card used in a ticket or a visa");
        return "redirect:/card/list";
    }
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
