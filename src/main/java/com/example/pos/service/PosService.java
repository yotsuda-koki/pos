package com.example.pos.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.transaction.Transactional;

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
    
    private final Map<String, TaxCalculator> taxCalculators;

    public int checkout(List<ItemRequest> items) {
        int total = 0;
        for (ItemRequest item : items) {
            String beanName = "FOOD".equals(item.getTaxType()) ? "zeroTax" : "standardTax";
            TaxCalculator calculator = taxCalculators.get(beanName);
            total += calculator.calculate(item.getPrice());
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

            String beanName = "FOOD".equals(product.getTaxCategory()) ? "zeroTax" : "standardTax";
            TaxCalculator calculator = taxCalculators.get(beanName);
            
            int inclusivePrice = calculator.calculate(product.getBasePrice());
            int taxAmount = inclusivePrice - product.getBasePrice();

            SaleDetailEntity detail = new SaleDetailEntity();
            detail.setSaleHeader(header);
            detail.setProductId(product.getId());
            detail.setQuantity(1);
            detail.setUnitPrice(product.getBasePrice());
            detail.setTaxRate(calculator.getRate()); // エラー解消
            details.add(detail);

            totalEx += product.getBasePrice();
            totalTax += taxAmount;
        }

        header.setDetails(details);
        header.setTotalExclusiveAmount(totalEx);
        header.setTotalTaxAmount(totalTax);
        header.setTotalInclusiveAmount(totalEx + totalTax);

        return saleHeaderRepository.save(header);
    }
}