package com.etlas.service.impl;

import com.etlas.service.CustomerService;
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
    private final CustomerService customerService;

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

        // total profit
        Map<String,BigDecimal> totalProfit = new HashMap<>();
        Map<String,BigDecimal> ticketTotalProfit = new HashMap<>();
        Map<String,BigDecimal> visaTotalProfit = new HashMap<>();

        BigDecimal totalTicketTRYProfit = ticketService.getTicketTRYTotalProfit(time);
        BigDecimal totalTicketUSDProfit = ticketService.getTicketUSDTotalProfit(time);
        BigDecimal totalTicketEURProfit = ticketService.getTicketEURTotalProfit(time);

        BigDecimal totalVisaTRYProfit = visaService.getVisaTRYTotalProfit(time);
        BigDecimal totalVisaUSDProfit = visaService.getVisaUSDTotalProfit(time);
        BigDecimal totalVisaEURProfit = visaService.getVisaEURTotalProfit(time);

        BigDecimal totalTRYProfit = totalTicketTRYProfit.add(totalVisaTRYProfit);
        BigDecimal totalUSDProfit = totalTicketUSDProfit.add(totalVisaUSDProfit);
        BigDecimal totalEURProfit = totalTicketEURProfit.add(totalVisaEURProfit);

        totalProfit.put("totalTRYProfit",totalTRYProfit);
        totalProfit.put("totalUSDProfit",totalUSDProfit);
        totalProfit.put("totalEURProfit",totalEURProfit);

        ticketTotalProfit.put("totalTicketTRYProfit",totalTicketTRYProfit);
        ticketTotalProfit.put("totalTicketUSDProfit",totalTicketUSDProfit);
        ticketTotalProfit.put("totalTicketEURProfit",totalTicketEURProfit);

        visaTotalProfit.put("totalVisaTRYProfit",totalVisaTRYProfit);
        visaTotalProfit.put("totalVisaUSDProfit",totalVisaUSDProfit);
        visaTotalProfit.put("totalVisaEURProfit",totalVisaEURProfit);


        // total unpaid
        Map<String,BigDecimal> totalUnpaid = new HashMap<>();

        BigDecimal totalTRYUnpaid = customerService.getTotalTRYUnpaid();
        BigDecimal totalUSDUnpaid = customerService.getTotalUSDUnpaid();
        BigDecimal totalEURUnpaid = customerService.getTotalEURUnpaid();

        totalUnpaid.put("totalTRYUnpaid",totalTRYUnpaid);
        totalUnpaid.put("totalUSDUnpaid",totalUSDUnpaid);
        totalUnpaid.put("totalEURUnpaid",totalEURUnpaid);

        return List.of(
                totalPerches, totalSales, totalProfit, totalUnpaid,
                ticketTotalPerches, ticketTotalSales, ticketTotalProfit,
                visaTotalPerches, visaTotalSales, visaTotalProfit
        );
    }

    @Override
    public List<Map<String, Integer>> getTotalNumbers(String time) {

        Map<String, Integer> totalNumberOfTicket = new HashMap<>();
        Map<String, Integer> totalNumberOfVisa = new HashMap<>();
        Map<String, Integer> totalNumber = new HashMap<>();


        // total number of ticket
        int totalTRYPerchesTicket = ticketService.getTotalTRYPerchesTicket(time);
        int totalUSDPerchesTicket = ticketService.getTotalUSDPerchesTicket(time);
        int totalEURPerchesTicket = ticketService.getTotalEURPerchesTicket(time);

        totalNumberOfTicket.put("totalTRYPerchesTicket",totalTRYPerchesTicket);
        totalNumberOfTicket.put("totalUSDPerchesTicket",totalUSDPerchesTicket);
        totalNumberOfTicket.put("totalEURPerchesTicket",totalEURPerchesTicket);


        // total number of visa
        int totalTRYPerchesVisa = visaService.getTotalTRYPerchesVisa(time);
        int totalUSDPerchesVisa = visaService.getTotalUSDPerchesVisa(time);
        int totalEURPerchesVisa = visaService.getTotalEURPerchesVisa(time);

        totalNumberOfVisa.put("totalTRYPerchesVisa",totalTRYPerchesVisa);
        totalNumberOfVisa.put("totalUSDPerchesVisa",totalUSDPerchesVisa);
        totalNumberOfVisa.put("totalEURPerchesVisa",totalEURPerchesVisa);

        Integer totalTicket = totalTRYPerchesTicket + totalUSDPerchesTicket + totalEURPerchesTicket;
        Integer totalVisa = totalTRYPerchesVisa + totalUSDPerchesVisa + totalEURPerchesVisa;
        totalNumber.put("totalTicket",totalTicket);
        totalNumber.put("totalVisa",totalVisa);

        return List.of(totalNumber,totalNumberOfTicket,totalNumberOfVisa);
    }

    @Override
    public String getTime(String time) {
        return switch (time) {
            case "lastMonth" -> "Last Month";
            case "thisYear" -> "This Year";
            case "lastYear" -> "Last Year";
            case "all" -> "All";
            default -> "This Month";
        };
    }
}
