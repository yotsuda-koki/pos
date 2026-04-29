package com.example.pos.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pos.domain.entity.SaleHeaderEntity;
import com.example.pos.model.DailySalesReportResponse;
import com.example.pos.repository.SaleHeaderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleReportService {

	private final SaleHeaderRepository saleHeaderRepository;
	
	// 指定された日付の日次売上レポートを作成する
	public DailySalesReportResponse getDailyReport(LocalDate date) {
		// 一日の始まり（00:00:00）と終わり（23:59:59）を定義
		LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        
        // 指定期間の売上データを一括取得
        List<SaleHeaderEntity> sales = saleHeaderRepository.findBySaleDatetimeBetween(startOfDay, endOfDay);
        
        // Stream APIを用いて売上合計と税額合計を集計
        int totalInclusive = sales.stream().mapToInt(SaleHeaderEntity::getTotalInclusiveAmount).sum();
        int totalTax = sales.stream().mapToInt(SaleHeaderEntity::getTotalTaxAmount).sum();

        // DTOにセットして返す
        DailySalesReportResponse response = new DailySalesReportResponse();
        response.setDate(date);
        response.setTotalSalesAmount(totalInclusive);
        response.setTotalTaxAmount(totalTax);
        response.setTransactionCount(sales.size());

        return response;
	}
}
