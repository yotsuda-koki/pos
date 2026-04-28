package com.example.pos.model;

import lombok.Data;

@Data
public class ItemRequest {
    private String name;
    private int price;
    private String taxType; // "FOOD" (0%) or "GENERAL" (10%)
}