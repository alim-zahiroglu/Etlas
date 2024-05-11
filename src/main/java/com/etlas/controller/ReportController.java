package com.etlas.controller;

import com.etlas.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reports")
@PreAuthorize("hasAnyAuthority('Admin','Manager')")
public class ReportController {
    private final ReportService reportService;

    @GetMapping
    public String getReports(Model model){

        Map<String, BigDecimal> ticketTotalPerches = reportService.getTicketTotalPerches();
        Map<String,BigDecimal> ticketTotalSales = reportService.getTicketTotalSales();
        Map<String,BigDecimal> ticketTotalProfit = reportService.getTicketTotalProfit();

        Map<String,BigDecimal> visaTotalPerches = reportService.getVisaTotalPerches();
        Map<String,BigDecimal> visaTotalSales = reportService.getVisaTotalSales();
        Map<String,BigDecimal> visaTotalProfit = reportService.getVisaTotalProfit();

        Map<String,BigDecimal> totalPerches = reportService.getTotalPerches(ticketTotalPerches, visaTotalPerches);
        Map<String,BigDecimal> totalSales = reportService.getTotalSales(ticketTotalSales,visaTotalSales);
        Map<String,BigDecimal> totalProfit = reportService.getTotalProfit(ticketTotalProfit,visaTotalProfit);
        Map<String,BigDecimal> totalUnpaid = reportService.getTotalUnpaid();

        model.addAttribute("totalPerches",totalPerches);
        model.addAttribute("totalSales",totalSales);
        model.addAttribute("totalProfit",totalProfit);
        model.addAttribute("totalUnpaid",totalUnpaid);

        model.addAttribute("ticketTotalPerches",ticketTotalPerches);
        model.addAttribute("ticketTotalSales",ticketTotalSales);
        model.addAttribute("ticketTotalProfit",ticketTotalProfit);

        model.addAttribute("visaTotalPerches",visaTotalPerches);
        model.addAttribute("visaTotalSales",visaTotalSales);
        model.addAttribute("visaTotalProfit",visaTotalProfit);
        return "report/reports";
    }
}
