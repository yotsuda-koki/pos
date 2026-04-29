package com.example.pos.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos.domain.entity.ProductEntity;
import com.example.pos.model.ProductRequest;
import com.example.pos.model.StockUpdateRequest;
import com.example.pos.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductEntity> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping
    public ProductEntity createProduct(@RequestBody ProductRequest request) {
        return productService.createProduct(request);
    }

    @PostMapping("/{janCode}/stock")
    public ProductEntity addStock(@PathVariable String janCode, @RequestBody StockUpdateRequest request) {
        return productService.addStock(janCode, request.getQuantity());
    }
}
