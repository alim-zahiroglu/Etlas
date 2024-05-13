package com.etlas.service.impl;

import com.etlas.dto.CardBalanceDto;
import com.etlas.dto.CardDto;
import com.etlas.entity.Bank;
import com.etlas.entity.Card;
import com.etlas.exception.CardNotFoundException;
import com.etlas.mapper.MapperUtil;
import com.etlas.repository.CardRepository;
import com.etlas.service.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl implements CardService {
    private final CardRepository repository;
    private final MapperUtil mapper;
    private final BankService bankService;
    private final TicketService ticketService;
    private final BalanceService balanceService;
    private final VisaService visaService;

    public CardServiceImpl(CardRepository repository, MapperUtil mapper, BankService bankService, @Lazy TicketService ticketService, BalanceService balanceService, @Lazy VisaService visaService) {
        this.repository = repository;
        this.mapper = mapper;
        this.bankService = bankService;
        this.ticketService = ticketService;
        this.balanceService = balanceService;
        this.visaService = visaService;
    }


    @Override
    public CardDto initiateNewCard() {
        return CardDto.builder()
                .availableLimitTRY(BigDecimal.ZERO)
                .availableLimitUSD(BigDecimal.ZERO)
                .availableLimitEUR(BigDecimal.ZERO)
                .build();
    }

    @Override
    public CardBalanceDto initiateForBalance() {
        List<Card> cardList =repository.findAllNonDeletedOrderByAvailableLimitTRY();
        CardDto minTryCardDto = new CardDto();
        if (!cardList.isEmpty()) {
            minTryCardDto = mapper.convert(cardList.get(0), new CardDto());
        }
        return CardBalanceDto.builder()
                .card(minTryCardDto)
                .tryBalance(BigDecimal.ZERO)
                .usdBalance(BigDecimal.ZERO)
                .eurBalance(BigDecimal.ZERO)
                .build();

    }
    @Override
    public CardBalanceDto singleCardInitiateFordBalance(String cardId) {
        Card oldCard = repository.findById(Long.parseLong(cardId)).orElseThrow(()-> new CardNotFoundException("No card found with id: " + cardId));
        return CardBalanceDto.builder()
                .card(mapper.convert(oldCard, new CardDto()))
                .tryBalance(BigDecimal.ZERO)
                .usdBalance(BigDecimal.ZERO)
                .eurBalance(BigDecimal.ZERO)
                .build();
    }

    @Override
    public CardDto findById(long paidCardId) {
       Card card = repository.findById(paidCardId).orElseThrow(()-> new CardNotFoundException("No card found with id: " + paidCardId));
       return mapper.convert(card, new CardDto());
    }

    @Override
    public CardDto saveNewCard(CardDto newCard) {
        CardDto card = prepareCardToSave(newCard);
        saveBankNames(newCard);
        Card savedCard = repository.save(mapper.convert(card, new Card()));
        return mapper.convert(savedCard, new CardDto());
    }
    private CardDto prepareCardToSave(CardDto newCard) {
        if (newCard.getAvailableLimitTRY() == null) {
            newCard.setAvailableLimitTRY(BigDecimal.ZERO);
        }
        if (newCard.getAvailableLimitUSD() == null) {
            newCard.setAvailableLimitUSD(BigDecimal.ZERO);
        }
        if (newCard.getAvailableLimitEUR() == null) {
            newCard.setAvailableLimitEUR(BigDecimal.ZERO);
        }
        return newCard;
    }

    @Override
    public CardDto updateCard(CardDto cardToBeUpdate) {
        return saveNewCard(cardToBeUpdate);
    }

    private void saveBankNames(CardDto newCard) {
        if (newCard.getBankName() != null) {
            String bankName = newCard.getBankName();
            if (!bankService.isBankNameExist(bankName)) {
                bankService.saveBankName(Bank.builder().bankName(bankName).build());
            }
        }
    }

    @Override
    public CardDto getCardById(long cardId) {
        Card foundCard = repository.findById(cardId).orElseThrow(()-> new CardNotFoundException("No card found with id: " + cardId));
        CardDto card = mapper.convert(foundCard, new CardDto());
        return prepareToUI(card);
    }

    @Override
    public List<CardDto> getAllCards() {

        List<Card> cardList = repository.findAllByIsDeletedOrderByAvailableLimitTRYDesc(false);
        return cardList.stream()
                .map(card -> mapper.convert(card, new CardDto()))
                .map(this::prepareToUI)
                .collect(Collectors.toList());
    }

    private CardDto prepareToUI(CardDto cardDto) {
        DecimalFormat formatter = new DecimalFormat("#,##0.00");

        //set available TRY balance to UI
        BigDecimal tryValue = cardDto.getAvailableLimitTRY();
        String formattedTRYValue = (!Objects.equals(tryValue, BigDecimal.ZERO)) ? formatter.format(tryValue) : "0.00";
        cardDto.setAvailableLimitTRYUI(formattedTRYValue);

        //set available USD balance to UI
        BigDecimal usdValue = cardDto.getAvailableLimitUSD();
        String formattedUSDValue = (!Objects.equals(usdValue, BigDecimal.ZERO)) ? formatter.format(usdValue) : "0.00";
        cardDto.setAvailableLimitUSDUI(formattedUSDValue);

        //set available EUR balance to UI
        BigDecimal eurValue = cardDto.getAvailableLimitEUR();
        String formattedEURValue = (!Objects.equals(eurValue, BigDecimal.ZERO)) ? formatter.format(eurValue) : "0.00";
        cardDto.setAvailableLimitEURUI(formattedEURValue);

        String[] date = cardDto.getDueDate().split("/");
        int dayOfMonth = Integer.parseInt(date[0]);
        int monthValue = LocalDate.now().getMonthValue();
        int dayValue = LocalDate.now().getDayOfMonth();

        if (dayOfMonth < dayValue && monthValue == 12) {
            monthValue = 1;
        } else if (dayOfMonth < dayValue) {
            monthValue++;
        }
        String result = String.format("%02d/%02d", dayOfMonth, monthValue);
        cardDto.setDueDate(result);
        return cardDto;
    }

    @Override
    public void saveCreditCard(CardDto creditCard) {
        repository.save(mapper.convert(creditCard, new Card()));
    }

    @Override
    public List<CardDto> findAllCardList() {
        List<Card> cardList = repository.findAllByIsDeletedOrderByAvailableLimitTRYDesc(false);
        return cardList.stream()
                .map(card -> mapper.convert(card, new CardDto()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isCardDeletable(String cardId) {
       boolean isCardUsedInTicket = ticketService.isCardUsedInAnyTicket(cardId);
       boolean isCardUsedInVisa = visaService.isCardUsedInAnyVisa(cardId);
       boolean isCardUsedInRecord = balanceService.isCardUsedInAnyRecord(cardId);
       return !isCardUsedInTicket && !isCardUsedInVisa && !isCardUsedInRecord;
    }

    @Override
    public CardDto deleteCard(String cardId) {
        Card card = repository.findById(Long.parseLong(cardId)).orElseThrow(()-> new CardNotFoundException("No card found with id: " + cardId));
        card.setDeleted(true);
        Card deletedCard = repository.save(card);
        return mapper.convert(deletedCard, new CardDto());
    }

    @Override
    public CardDto addBalance(CardBalanceDto cardBalanceDto) {
        long cardId = cardBalanceDto.getCard().getId();
        Card oldCard = repository.findById(cardId).orElseThrow(()-> new CardNotFoundException("No card found with id: " + cardId));

        if(cardBalanceDto.getTryBalance() == null) cardBalanceDto.setTryBalance(BigDecimal.ZERO);
        if(cardBalanceDto.getUsdBalance() == null) cardBalanceDto.setUsdBalance(BigDecimal.ZERO);
        if(cardBalanceDto.getEurBalance() == null) cardBalanceDto.setEurBalance(BigDecimal.ZERO);

        oldCard.setAvailableLimitTRY(oldCard.getAvailableLimitTRY().add(cardBalanceDto.getTryBalance()));
        oldCard.setAvailableLimitUSD(oldCard.getAvailableLimitUSD().add(cardBalanceDto.getUsdBalance()));
        oldCard.setAvailableLimitEUR(oldCard.getAvailableLimitEUR().add(cardBalanceDto.getEurBalance()));

        Card updatedCard = repository.save(oldCard);
        return mapper.convert(updatedCard, new CardDto());
    }
}
