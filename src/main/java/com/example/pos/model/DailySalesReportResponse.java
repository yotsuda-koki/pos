package com.example.pos.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class DailySalesReportResponse {
	private LocalDate date;
	private int totalSalesAmount;
	private int totalTaxAmount;
	private int transactionCount;
}
