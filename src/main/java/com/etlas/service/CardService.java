package com.etlas.service;

import com.etlas.dto.CardBalanceDto;
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

    CardBalanceDto initiateForBalance();

    CardDto addBalance(CardBalanceDto cardBalanceDto);

    CardBalanceDto singleCardInitiateFordBalance(String cardId);

    CardDto getCardById(long parseLong);
}
