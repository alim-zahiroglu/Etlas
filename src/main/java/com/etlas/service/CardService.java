package com.etlas.service;

import com.etlas.dto.CardDto;

import java.util.List;

public interface CardService {
    CardDto saveNewCard(CardDto newCard);
    List<CardDto> getAllCards();
}
