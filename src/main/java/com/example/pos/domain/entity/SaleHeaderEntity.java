package com.example.pos.domain.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "t_sales")
@Data
public class SaleHeaderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime saleDatetime;
    private int totalExclusiveAmount;
    private int totalTaxAmount;
    private int totalInclusiveAmount;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "saleHeader")
    private List<SaleDetailEntity> details;
}