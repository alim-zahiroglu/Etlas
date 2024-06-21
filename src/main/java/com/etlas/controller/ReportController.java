package com.etlas.controller;

import com.etlas.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reports")
@PreAuthorize("hasAnyAuthority('Admin','Manager')")
public class ReportController {
    private final ReportService reportService;

    @GetMapping()
    public String getReports(@RequestParam(value = "time",required = false, defaultValue = "thisMonth") String time, Model model){
        List<Map<String,BigDecimal>> reportingData = reportService.getReports(time);
        List<Map<String,Integer>> totalNumberInfo = reportService.getTotalNumbers(time);
        String atTime = reportService.getTime(time);
        model.addAttribute("atTime",atTime);
        model.addAttribute("reportData",reportingData);
        model.addAttribute("totalNumberInfo",totalNumberInfo);
        return "report/reports";
    }
}
