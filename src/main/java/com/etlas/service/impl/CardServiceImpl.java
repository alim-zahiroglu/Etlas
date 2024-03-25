package com.etlas.service.impl;

import com.etlas.dto.CardDto;
import com.etlas.entity.Card;
import com.etlas.mapper.MapperUtil;
import com.etlas.repository.CardRepository;
import com.etlas.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository repository;
    private final MapperUtil mapper;


    @Override
    public CardDto saveNewCard(CardDto newCard) {
        Card savedCard = repository.save(mapper.convert(newCard, new Card()));
        return mapper.convert(savedCard, new CardDto());
    }
}
