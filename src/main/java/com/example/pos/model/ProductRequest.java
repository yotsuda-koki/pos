package com.example.pos.model;

import lombok.Data;

@Data
public class ProductRequest {
    private String janCode;
    private String name;
    private int basePrice;
    private int stock;
    private Long taxCategoryId;
}