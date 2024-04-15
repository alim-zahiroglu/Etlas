package com.etlas.converter;

import com.etlas.dto.CardDto;
import com.etlas.service.CardService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CardConverter implements Converter<String, CardDto> {
    private final CardService cardService;

    public CardConverter(CardService cardService) {
        this.cardService = cardService;
    }

    @Override
    public CardDto convert(String cardId) {
        if (cardId.equals("") || cardId.equals("0")) {
            return null;
        }
        long id = Long.parseLong(cardId);
        return cardService.findById(id);
    }
}
