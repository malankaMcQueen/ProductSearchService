package com.example.Product.repository;

import com.example.Product.model.Product;
import com.example.Product.model.SKU;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkuRepository extends JpaRepository<SKU, Long> {
}

