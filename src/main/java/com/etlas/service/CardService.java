package com.etlas.service;

import com.etlas.dto.CardDto;

import java.util.List;

public interface CardService {
    CardDto saveNewCard(CardDto newCard);
    List<CardDto> getAllCards();

    boolean isCardDeletable(String cardId);

    CardDto deleteCard(String cardId);

    List<CardDto> findAllCardList();

    CardDto findById(long paidCardId);

    void saveCreditCard(CardDto creditCard);

    CardDto initiateNewCard();

    CardDto updateCard(CardDto cardToBeUpdate);
}
