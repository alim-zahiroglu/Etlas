package com.etlas.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ReportService {

    List<Map<String, BigDecimal>> getReports(String time);

    List<Map<String, Integer>> getTotalNumbers(String time);

    String getTime(String time);
}
