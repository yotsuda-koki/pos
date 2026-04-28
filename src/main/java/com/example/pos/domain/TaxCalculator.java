package com.example.pos.domain;

import org.springframework.stereotype.Component;

public interface TaxCalculator {
    int calculate(int price);
}

@Component("zeroTax")
class ZeroTaxStrategy implements TaxCalculator {
    @Override public int calculate(int price) { return price; }
}

@Component("standardTax")
class StandardTaxStrategy implements TaxCalculator {
    @Override public int calculate(int price) { return (int) (price * 1.1); }
}