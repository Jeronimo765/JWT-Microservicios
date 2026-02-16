package com.business.service;

import com.business.dto.ProductDTO;
import java.util.List;

public interface ProductService {
    ProductDTO create(ProductDTO dto);
    ProductDTO update(Long id, ProductDTO dto);
    ProductDTO findById(Long id);
    List<ProductDTO> findAll();
    List<ProductDTO> findActive();
    List<ProductDTO> findByCategory(Long categoryId);
    List<ProductDTO> findBySupplier(Long supplierId);
    void delete(Long id);
}