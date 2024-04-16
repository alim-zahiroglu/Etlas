package com.etlas.converter;

import com.etlas.dto.TicketDto;
import com.etlas.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketConverter implements Converter<String, TicketDto> {
    private final TicketService ticketService;
    @Override
    public TicketDto convert(String ticketId) {
        if (ticketId.equals("") || ticketId.equals("0")) {
            return null;
        }
        long id = Long.parseLong(ticketId);
        return ticketService.findById(id);
    }
}
