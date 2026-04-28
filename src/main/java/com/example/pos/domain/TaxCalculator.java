package com.example.pos.domain;

import org.springframework.stereotype.Component;

public interface TaxCalculator {
    int calculate(int price);
    double getRate();
}

@Component("zeroTax")
class ZeroTaxStrategy implements TaxCalculator {
    @Override public int calculate(int price) { return price; }
    @Override public double getRate() { return 0.0; }
}

@Component("standardTax")
class StandardTaxStrategy implements TaxCalculator {
    @Override public int calculate(int price) { return (int) (price * 1.1); }
    @Override public double getRate() { return 0.1; }
}