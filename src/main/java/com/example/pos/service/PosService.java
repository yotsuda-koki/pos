package com.example.pos.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.example.pos.domain.TaxCalculator;
import com.example.pos.domain.entity.ProductEntity;
import com.example.pos.domain.entity.SaleDetailEntity;
import com.example.pos.domain.entity.SaleHeaderEntity;
import com.example.pos.model.ItemRequest;
import com.example.pos.repository.ProductRepository;
import com.example.pos.repository.SaleHeaderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PosService {
	private final ProductRepository productRepository;
    private final SaleHeaderRepository saleHeaderRepository;
    private final ApplicationContext context;

    public int checkout(List<ItemRequest> items) {
        int total = 0;
        for (ItemRequest item : items) {
            // 税区分に応じてBean（計算機）を使い分ける
            String beanName = "FOOD".equals(item.getTaxType()) ? "zeroTax" : "standardTax";
            TaxCalculator calculator = context.getBean(beanName, TaxCalculator.class);
            total += calculator.calculate(item.getPrice());
        }
        return total;
    }
    
    public int checkoutByJanCodes(List<String> janCodes) {
        int total = 0;
        for (String janCode : janCodes) {
            // DBから商品を検索
            ProductEntity entity = productRepository.findByJanCode(janCode)
                .orElseThrow(() -> new RuntimeException("商品が見つかりません: " + janCode));

            // 税計算戦略の決定
            String beanName = "FOOD".equals(entity.getTaxCategory()) ? "zeroTax" : "standardTax";
            TaxCalculator calculator = context.getBean(beanName, TaxCalculator.class);
            
            total += calculator.calculate(entity.getBasePrice());
        }
        return total;
    }
    
    @Transactional
    public SaleHeaderEntity processSale(List<String> janCodes) {
        SaleHeaderEntity header = new SaleHeaderEntity();
        header.setSaleDatetime(LocalDateTime.now());
        List<SaleDetailEntity> details = new ArrayList<>();

        int totalEx = 0;
        int totalTax = 0;

        for (String janCode : janCodes) {
            ProductEntity product = productRepository.findByJanCode(janCode)
                .orElseThrow(() -> new RuntimeException("商品未登録: " + janCode));

            // 戦略に基づいて計算
            String beanName = "FOOD".equals(product.getTaxCategory()) ? "zeroTax" : "standardTax";
            TaxCalculator calculator = context.getBean(beanName, TaxCalculator.class);
            
            int inclusivePrice = calculator.calculate(product.getBasePrice());
            int taxAmount = inclusivePrice - product.getBasePrice();

            // 明細の作成
            SaleDetailEntity detail = new SaleDetailEntity();
            detail.setSaleHeader(header);
            detail.setProductId(product.getId());
            detail.setQuantity(1);
            detail.setUnitPrice(product.getBasePrice());
            detail.setTaxRate(calculator.getRate());
            details.add(detail);

            totalEx += product.getBasePrice();
            totalTax += taxAmount;
        }

        header.setDetails(details);
        header.setTotalExclusiveAmount(totalEx);
        header.setTotalTaxAmount(totalTax);
        header.setTotalInclusiveAmount(totalEx + totalTax);

        // まとめて保存（CascadeType.ALLにより明細も保存される）
        return saleHeaderRepository.save(header);
    }
}