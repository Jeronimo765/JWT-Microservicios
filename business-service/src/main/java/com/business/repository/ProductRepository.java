package com.business.repository;

import com.business.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySku(String sku);
    List<Product> findByIsActiveTrue();
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findBySupplierId(Long supplierId);
    
    @Query("SELECT p FROM Product p WHERE p.stockQuantity < :minStock")
    List<Product> findLowStockProducts(Integer minStock);
    
    Boolean existsBySku(String sku);
}