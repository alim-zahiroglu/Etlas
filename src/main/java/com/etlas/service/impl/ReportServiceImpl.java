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

        Map<String,BigDecimal> total = new HashMap<>();

        BigDecimal totalTicketTRYPerches = ticketService.getTicketTRYTotalPerches(time);
        BigDecimal totalTicketUSDPerches = ticketService.getTicketUSDTotalPerches(time);
        BigDecimal totalTicketEURPerches = ticketService.getTicketEURTotalPerches(time);

        BigDecimal totalVisaTRYPerches = visaService.getVisaTRYTotalPerches(time);
        BigDecimal totalVisaUSDPerches = visaService.getVisaUSDTotalPerches(time);
        BigDecimal totalVisaEURPerches = visaService.getVisaEURTotalPerches(time);

        BigDecimal totalTRYPerches = totalTicketTRYPerches.add(totalVisaTRYPerches);
        BigDecimal totalUSDPerches = totalTicketUSDPerches.add(totalVisaUSDPerches);
        BigDecimal totalEURPerches = totalTicketEURPerches.add(totalVisaEURPerches);

        total.put("totalTRYPerches",totalTRYPerches);
        total.put("totalUSDPerches",totalUSDPerches);
        total.put("totalEURPerches",totalEURPerches);


        return List.of(
                total
        );
    }
}
