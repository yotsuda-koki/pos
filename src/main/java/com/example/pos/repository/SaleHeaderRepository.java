package com.example.pos.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pos.domain.entity.SaleHeaderEntity;

@Repository
public interface SaleHeaderRepository extends JpaRepository<SaleHeaderEntity, Long> {
	List<SaleHeaderEntity> findBySaleDatetimeBetween(LocalDateTime start, LocalDateTime end);
}