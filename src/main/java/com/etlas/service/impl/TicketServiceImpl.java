package com.etlas.service.impl;

import com.etlas.dto.AirLineDto;
import com.etlas.dto.TicketDto;
import com.etlas.dto.UserDto;
import com.etlas.entity.User;
import com.etlas.enums.CurrencyUnits;
import com.etlas.service.AirLineService;
import com.etlas.service.SecurityService;
import com.etlas.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final AirLineService airLineService;
    private final SecurityService securityService;
    @Override
    public TicketDto initializeNewTicket() {
        AirLineDto turkishAirline = airLineService.findByName("Turkish Airlines");
        UserDto currentUser = securityService.getLoggedInUser();
        return TicketDto.builder()
                .airLine(turkishAirline)
                .singleTicket(true)
                .oneWayTrip(true)
                .boughtUser(currentUser)
                .receivedUser(currentUser)
                .currencyUnit(CurrencyUnits.TRY)
                .build();
    }
}
