package com.business.service.impl;

import com.business.dto.ProductDTO;
import com.business.entity.Category;
import com.business.entity.Product;
import com.business.entity.Supplier;
import com.business.repository.CategoryRepository;
import com.business.repository.ProductRepository;
import com.business.repository.SupplierRepository;
import com.business.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;

    @Override
    @Transactional
    public ProductDTO create(ProductDTO dto) {
        if (dto.getSku() != null && productRepository.existsBySku(dto.getSku())) {
            throw new RuntimeException("Product with SKU '" + dto.getSku() + "' already exists");
        }

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setSku(dto.getSku());
        product.setIsActive(dto.getIsActive());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + dto.getCategoryId()));
            product.setCategory(category);
        }

        if (dto.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                    .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + dto.getSupplierId()));
            product.setSupplier(supplier);
        }

        Product saved = productRepository.save(product);
        return toDTO(saved);
    }

    @Override
    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        if (dto.getSku() != null && !dto.getSku().equals(product.getSku()) 
                && productRepository.existsBySku(dto.getSku())) {
            throw new RuntimeException("Product with SKU '" + dto.getSku() + "' already exists");
        }

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setSku(dto.getSku());
        product.setIsActive(dto.getIsActive());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + dto.getCategoryId()));
            product.setCategory(category);
        }

        if (dto.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                    .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + dto.getSupplierId()));
            product.setSupplier(supplier);
        }

        Product updated = productRepository.save(product);
        return toDTO(updated);
    }

    @Override
    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return toDTO(product);
    }

    @Override
    public List<ProductDTO> findAll() {
        return productRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> findActive() {
        return productRepository.findByIsActiveTrue().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> findByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> findBySupplier(Long supplierId) {
        return productRepository.findBySupplierId(supplierId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    private ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setSku(product.getSku());
        dto.setIsActive(product.getIsActive());

        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }

        if (product.getSupplier() != null) {
            dto.setSupplierId(product.getSupplier().getId());
            dto.setSupplierName(product.getSupplier().getName());
        }

        return dto;
    }
}