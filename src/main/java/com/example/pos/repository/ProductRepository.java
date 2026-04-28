package com.example.pos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pos.domain.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    // JANコードで商品を検索するメソッドを定義
    Optional<ProductEntity> findByJanCode(String janCode);
}