package com.etlas.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reports")
@PreAuthorize("hasAnyAuthority('Admin','Manager')")
public class ReportController {

    @GetMapping
    public String getReports(){
        return "report/reports";
    }
}
