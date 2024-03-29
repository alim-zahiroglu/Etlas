package com.etlas.controller;

import com.etlas.dto.CardBalanceDto;
import com.etlas.dto.CardDto;
import com.etlas.service.BankService;
import com.etlas.service.CardService;
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
    public String deleteCard(@RequestParam("cardId") String cardId, @RequestParam("from") String from,
                             RedirectAttributes redirectAttributes){
        if (cardService.isCardDeletable(cardId)) {
            CardDto deletedCard = cardService.deleteCard(cardId);
            redirectAttributes.addFlashAttribute("isCardDeleted", true);
            redirectAttributes.addFlashAttribute("deleteCardName", deletedCard.getCardOwner());

            if (from.equals("card")) return "redirect:/card/list/card";

            return "redirect:/card/list";
        }
        redirectAttributes.addFlashAttribute("isCardDeleted", false);
        redirectAttributes.addFlashAttribute("deleteMessage", "Because this card used in a ticket or a visa");

        if (from.equals("card")) return "redirect:/card/list/card";
        return "redirect:/card/list";
    }

    @GetMapping("/update/{cardId}")
    public String updateCard(@PathVariable("cardId") long id, @Param("from") String from, Model model){
        CardDto cardToBeUpdate = cardService.findById(id);
        cardToBeUpdate.setFromForUpdateUI(from);

        model.addAttribute("cardToBeUpdate",cardToBeUpdate);
        model.addAttribute("bankNames", bankService.getAllBankNames());
        model.addAttribute("from", from);
        return "card/card-update";
    }

    @PostMapping("/update/{id}")
    public String saveUpdatedUser(@Valid @ModelAttribute("cardToBeUpdate") CardDto cardToBeUpdate, BindingResult bindingResult,
                                  @Param("from") String from,
                                  Model model, RedirectAttributes redirectAttributes){

        if (bindingResult.hasErrors()){
            model.addAttribute("bankNames", bankService.getAllBankNames());
            model.addAttribute("from", from);
            return "card/card-update";
        }
        CardDto updatedCard = cardService.updateCard(cardToBeUpdate);
        redirectAttributes.addFlashAttribute("isCardUpdated", true);
        redirectAttributes.addFlashAttribute("updatedCardName", updatedCard.getCardOwner());

        if (cardToBeUpdate.getFromForUpdateUI().equals("card")) return "redirect:/card/list/card";
        if (from.equals("ticket")) return "redirect:/ticket/list";
        return "redirect:/card/list";
    }

    @GetMapping("/addBalance")
    public String cardAddBalance(Model model){

        model.addAttribute("balance", cardService.initiateFordBalance());
        model.addAttribute("cardList", cardService.getAllCards());
        return "card/add-balance";
    }
    @PostMapping("/addBalance")
    public String saveBalanceAddedCard(@Valid @ModelAttribute("balance")
                                       CardBalanceDto cardBalanceDto, BindingResult bindingResult,
                                       Model model, RedirectAttributes redirectAttributes){

        if (bindingResult.hasErrors()){
            model.addAttribute("cardList", cardService.getAllCards());
            return "card/add-balance";
        }

        CardDto balanceAddedCard = cardService.addBalance(cardBalanceDto);
        redirectAttributes.addFlashAttribute("isBalanceAdded", true);
        redirectAttributes.addFlashAttribute("balanceAddedCard", balanceAddedCard);
        return "redirect:/card/addBalance";
    }
    @GetMapping("/addBalance/{cardId}")
    public String singleCardAddBalance(@PathVariable String cardId,
                                       @Param("from") String from, Model model){

        model.addAttribute("balance", cardService.singleCardInitiateFordBalance(cardId));
        model.addAttribute("cardList", cardService.getAllCards());
        model.addAttribute("from", from);

        if (from.equals("card")) return "card/add-balance-card-view";

        return "card/add-balance-list-view";
    }

    @PostMapping("/addBalance/{cardId}")
    public String singleCardAddBalanceSave(@ModelAttribute("balance")CardBalanceDto cardBalanceDto,
                                           @PathVariable String cardId,
                                           @Param("from") String from, RedirectAttributes redirectAttributes){

        cardBalanceDto.setCard(cardService.findById(Long.parseLong(cardId)));
        CardDto balanceAddedCard = cardService.addBalance(cardBalanceDto);

        redirectAttributes.addFlashAttribute("isBalanceAdded", true);
        redirectAttributes.addFlashAttribute("balanceAddedCard", balanceAddedCard);

        if (from.equals("card")) return "redirect:/card/list/card";
        return "redirect:/card/list";
    }

    @GetMapping("/details/{cardId}")
    public String cardDetail(@PathVariable String cardId,
                             @Param("from") String from, Model model) {
        CardDto card = cardService.getCardById(Long.parseLong(cardId));
        model.addAttribute("card",card);
        System.out.println(card.getAvailableLimitTRYUI());
        System.out.println(card.getAvailableLimitUSDUI());
        System.out.println(card.getAvailableLimitEURUI());
        model.addAttribute("from",from);
        return "card/card-details";
    }
}
