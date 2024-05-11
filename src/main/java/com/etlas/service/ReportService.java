package com.etlas.service;

import java.math.BigDecimal;
import java.util.Map;

public interface ReportService {

    Map<String, BigDecimal> getTicketTotalPerches();

    Map<String, BigDecimal> getTicketTotalSales();

    Map<String, BigDecimal> getTicketTotalProfit();

    Map<String, BigDecimal> getVisaTotalPerches();

    Map<String, BigDecimal> getVisaTotalSales();

    Map<String, BigDecimal> getVisaTotalProfit();

    Map<String, BigDecimal> getTotalPerches(Map<String, BigDecimal> ticketTotalPerches, Map<String, BigDecimal> visaTotalPerches);

    Map<String, BigDecimal> getTotalSales(Map<String, BigDecimal> ticketTotalSales, Map<String, BigDecimal> visaTotalSales);

    Map<String, BigDecimal> getTotalProfit(Map<String, BigDecimal> ticketTotalProfit, Map<String, BigDecimal> visaTotalProfit);

    Map<String, BigDecimal> getTotalUnpaid();
}
