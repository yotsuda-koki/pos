package com.example.pos.model;

import lombok.Data;

@Data
public class CartResponse {
    private int totalAmount;
    private String message;
}