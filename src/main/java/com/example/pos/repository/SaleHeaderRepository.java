package com.example.pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pos.domain.entity.SaleHeaderEntity;

@Repository
public interface SaleHeaderRepository extends JpaRepository<SaleHeaderEntity, Long> {
}