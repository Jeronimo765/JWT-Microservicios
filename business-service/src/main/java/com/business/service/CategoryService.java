package com.business.service;

import com.business.dto.CategoryDTO;
import java.util.List;

public interface CategoryService {
    CategoryDTO create(CategoryDTO dto);
    CategoryDTO update(Long id, CategoryDTO dto);
    CategoryDTO findById(Long id);
    List<CategoryDTO> findAll();
    List<CategoryDTO> findActive();
    void delete(Long id);
}