package com.etlas.service.impl;

import com.etlas.service.ReportService;
import com.etlas.service.TicketService;
import com.etlas.service.VisaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {
    private final TicketService ticketService;
    private final VisaService visaService;

    @Override
    public Map<String, BigDecimal> getTicketTotalPerches() {
        BigDecimal ticketTRYTotalPerches = ticketService.getTicketTRYTotalPerches();
        BigDecimal ticketUSDTotalPerches = ticketService.getTicketUSDTotalPerches();
        BigDecimal ticketEURTotalPerches = ticketService.getTicketEURTotalPerches();

        return Map.of("ticketTRYTotalPerches",ticketTRYTotalPerches,
                      "ticketUSDTotalPerches",ticketUSDTotalPerches,
                      "ticketEURTotalPerches",ticketEURTotalPerches);
    }

    @Override
    public Map<String, BigDecimal> getTicketTotalSales() {
        return Map.of();
    }

    @Override
    public Map<String, BigDecimal> getTicketTotalProfit() {
        return Map.of();
    }

    @Override
    public Map<String, BigDecimal> getVisaTotalPerches() {
        return Map.of();
    }

    @Override
    public Map<String, BigDecimal> getVisaTotalSales() {
        return Map.of();
    }

    @Override
    public Map<String, BigDecimal> getVisaTotalProfit() {
        return Map.of();
    }

    @Override
    public Map<String, BigDecimal> getTotalPerches(Map<String, BigDecimal> ticketTotalPerches, Map<String, BigDecimal> visaTotalPerches) {
        return Map.of();
    }

    @Override
    public Map<String, BigDecimal> getTotalSales(Map<String, BigDecimal> ticketTotalSales, Map<String, BigDecimal> visaTotalSales) {
        return Map.of();
    }

    @Override
    public Map<String, BigDecimal> getTotalProfit(Map<String, BigDecimal> ticketTotalProfit, Map<String, BigDecimal> visaTotalProfit) {
        return Map.of();
    }

    @Override
    public Map<String, BigDecimal> getTotalUnpaid() {
        return Map.of();
    }
}
