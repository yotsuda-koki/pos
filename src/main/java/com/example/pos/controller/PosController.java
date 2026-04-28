package com.example.pos.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos.model.CartResponse;
import com.example.pos.model.ItemRequest;
import com.example.pos.service.PosService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // Reactの開発サーバーを許可
public class PosController {
    private final PosService posService;

    @PostMapping("/checkout")
    public CartResponse checkout(@RequestBody List<ItemRequest> items) {
        int total = posService.checkout(items);
        CartResponse response = new CartResponse();
        response.setTotalAmount(total);
        response.setMessage("決済が完了しました（食料品非課税対応済み）");
        return response;
    }
}