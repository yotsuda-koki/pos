package com.example.pos.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "m_products")
@Data
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String janCode;

    @Column(nullable = false)
    private String name;

    private int basePrice;
    
    @Column(nullable = false)
    private int stock;

    @ManyToOne
    @JoinColumn(name="tax_category_id", nullable = false)
    private TaxCategoryEntity taxCategory;
}