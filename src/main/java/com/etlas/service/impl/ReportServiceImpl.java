package com.etlas.service.impl;

import com.etlas.service.ReportService;
import com.etlas.service.TicketService;
import com.etlas.service.VisaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {
    private final TicketService ticketService;
    private final VisaService visaService;

    @Override
    public List<Map<String, BigDecimal>> getReports(String time) {

        Map<String,BigDecimal> totalPerches = new HashMap<>();
        Map<String,BigDecimal> ticketTotalPerches = new HashMap<>();
        Map<String,BigDecimal> visaTotalPerches = new HashMap<>();

        BigDecimal totalTicketTRYPerches = ticketService.getTicketTRYTotalPerches(time);
        BigDecimal totalTicketUSDPerches = ticketService.getTicketUSDTotalPerches(time);
        BigDecimal totalTicketEURPerches = ticketService.getTicketEURTotalPerches(time);

        BigDecimal totalVisaTRYPerches = visaService.getVisaTRYTotalPerches(time);
        BigDecimal totalVisaUSDPerches = visaService.getVisaUSDTotalPerches(time);
        BigDecimal totalVisaEURPerches = visaService.getVisaEURTotalPerches(time);

        BigDecimal totalTRYPerches = totalTicketTRYPerches.add(totalVisaTRYPerches);
        BigDecimal totalUSDPerches = totalTicketUSDPerches.add(totalVisaUSDPerches);
        BigDecimal totalEURPerches = totalTicketEURPerches.add(totalVisaEURPerches);

        totalPerches.put("totalTRYPerches",totalTRYPerches);
        totalPerches.put("totalUSDPerches",totalUSDPerches);
        totalPerches.put("totalEURPerches",totalEURPerches);

        ticketTotalPerches.put("totalTicketTRYPerches",totalTicketTRYPerches);
        ticketTotalPerches.put("totalTicketUSDPerches",totalTicketUSDPerches);
        ticketTotalPerches.put("totalTicketEURPerches",totalTicketEURPerches);

        visaTotalPerches.put("totalVisaTRYPerches",totalVisaTRYPerches);
        visaTotalPerches.put("totalVisaUSDPerches",totalVisaUSDPerches);
        visaTotalPerches.put("totalVisaEURPerches",totalVisaEURPerches);

        // total sales

        Map<String,BigDecimal> totalSales = new HashMap<>();
        Map<String,BigDecimal> ticketTotalSales = new HashMap<>();
        Map<String,BigDecimal> visaTotalSales = new HashMap<>();

        BigDecimal totalTicketTRYSales = ticketService.getTicketTRYTotalSales(time);
        BigDecimal totalTicketUSDSales = ticketService.getTicketUSDTotalSales(time);
        BigDecimal totalTicketEURSales = ticketService.getTicketEURTotalSales(time);

        BigDecimal totalVisaTRYSales = visaService.getVisaTRYTotalSales(time);
        BigDecimal totalVisaUSDSales = visaService.getVisaUSDTotalSales(time);
        BigDecimal totalVisaEURSales = visaService.getVisaEURTotalSales(time);

        BigDecimal totalTRYSales = totalTicketTRYSales.add(totalVisaTRYSales);
        BigDecimal totalUSDSales = totalTicketUSDSales.add(totalVisaUSDSales);
        BigDecimal totalEURSales = totalTicketEURSales.add(totalVisaEURSales);

        totalSales.put("totalTRYSales",totalTRYSales);
        totalSales.put("totalUSDSales",totalUSDSales);
        totalSales.put("totalEURSales",totalEURSales);

        ticketTotalSales.put("totalTicketTRYSales",totalTicketTRYSales);
        ticketTotalSales.put("totalTicketUSDSales",totalTicketUSDSales);
        ticketTotalSales.put("totalTicketEURSales",totalTicketEURSales);

        visaTotalSales.put("totalVisaTRYSales",totalVisaTRYSales);
        visaTotalSales.put("totalVisaUSDSales",totalVisaUSDSales);
        visaTotalSales.put("totalVisaEURSales",totalVisaEURSales);

        return List.of(
                totalPerches, ticketTotalPerches, visaTotalSales,
                totalSales, ticketTotalSales, visaTotalSales
        );
    }
}
