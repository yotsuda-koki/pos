package com.example.pos.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos.model.DailySalesReportResponse;
import com.example.pos.service.SaleReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sales/report")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class SaleReportController {

    private final SaleReportService saleReportService;

    // GET /api/sales/report/daily?date=2026-04-29 の形式でアクセス
    @GetMapping("/daily")
    public DailySalesReportResponse getDailyReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        // 日付指定がない場合は「今日」を集計対象にする
        if (date == null) {
            date = LocalDate.now();
        }
        return saleReportService.getDailyReport(date);
    }
}
