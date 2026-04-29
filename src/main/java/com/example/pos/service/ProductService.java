package com.example.pos.service;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.pos.domain.entity.ProductEntity;
import com.example.pos.domain.entity.TaxCategoryEntity;
import com.example.pos.model.ProductRequest;
import com.example.pos.repository.ProductRepository;
import com.example.pos.repository.TaxCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final TaxCategoryRepository taxCategoryRepository;

    // 全商品の一覧取得
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    // 商品の新規登録
    @Transactional
    public ProductEntity createProduct(ProductRequest request) {
        TaxCategoryEntity taxCategory = taxCategoryRepository.findById(request.getTaxCategoryId())
                .orElseThrow(() -> new RuntimeException("指定された税区分が存在しません"));

        ProductEntity product = new ProductEntity();
        product.setJanCode(request.getJanCode());
        product.setName(request.getName());
        product.setBasePrice(request.getBasePrice());
        product.setStock(request.getStock());
        product.setTaxCategory(taxCategory);

        return productRepository.save(product);
    }

    // 在庫の補充
    @Transactional
    public ProductEntity addStock(String janCode, int quantity) {
        ProductEntity product = productRepository.findByJanCode(janCode)
                .orElseThrow(() -> new RuntimeException("商品未登録: " + janCode));
        
        // 現在の在庫数に、入荷数量を加算
        product.setStock(product.getStock() + quantity);
        return productRepository.save(product);
    }
}