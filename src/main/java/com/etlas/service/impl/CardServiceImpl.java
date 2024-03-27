package com.etlas.service.impl;

import com.etlas.dto.CardDto;
import com.etlas.entity.Card;
import com.etlas.mapper.MapperUtil;
import com.etlas.repository.CardRepository;
import com.etlas.service.CardService;
import com.etlas.service.TicketService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl implements CardService {
    private final CardRepository repository;
    private final MapperUtil mapper;
    private final TicketService ticketService;

    public CardServiceImpl(CardRepository repository, MapperUtil mapper, @Lazy TicketService ticketService) {
        this.repository = repository;
        this.mapper = mapper;
        this.ticketService = ticketService;
    }


    @Override
    public CardDto findById(long paidCardId) {
       Card card = repository.findById(paidCardId).orElseThrow(NoSuchElementException::new);
       return mapper.convert(card, new CardDto());
    }

    @Override
    public CardDto saveNewCard(CardDto newCard) {
        Card savedCard = repository.save(mapper.convert(newCard, new Card()));
        return mapper.convert(savedCard, new CardDto());
    }

    @Override
    public List<CardDto> getAllCards() {
        int monthValue = LocalDate.now().getMonthValue();
        List<Card> cardList = repository.findAllByIsDeletedOrderByAvailableLimitDesc(false);
        return cardList.stream()
                .map(card -> mapper.convert(card, new CardDto()))
                .map(cardDto -> {
                    int dayOfMonth = cardDto.getDueDate().getDayOfMonth();
                    DecimalFormat formatter = new DecimalFormat("#,###.00");
                    String formattedValue = formatter.format(cardDto.getAvailableLimit());
                    cardDto.setAvailableLimitUI(formattedValue);

                    String result = String.format("%02d/%02d", dayOfMonth, monthValue);
                    cardDto.setDueDateUI(result);
                    return cardDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void saveCreditCard(CardDto creditCard) {
        repository.save(mapper.convert(creditCard, new Card()));
    }

    @Override
    public List<CardDto> findAllCardList() {
        List<Card> cardList = repository.findAllByIsDeletedOrderByAvailableLimitDesc(false);
        return cardList.stream()
                .map(card -> mapper.convert(card, new CardDto()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isCardDeletable(String cardId) {
       return !ticketService.isCardUsedInAnyTicket(cardId);
    }

    @Override
    public CardDto deleteCard(String cardId) {
        Card card = repository.findById(Long.parseLong(cardId)).orElseThrow(NoSuchElementException::new);
        card.setDeleted(true);
        Card deletedCard = repository.save(card);
        return mapper.convert(deletedCard, new CardDto());
    }
}
