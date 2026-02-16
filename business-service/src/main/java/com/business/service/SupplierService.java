package com.business.service;

import com.business.dto.SupplierDTO;
import java.util.List;

public interface SupplierService {
    SupplierDTO create(SupplierDTO dto);
    SupplierDTO update(Long id, SupplierDTO dto);
    SupplierDTO findById(Long id);
    List<SupplierDTO> findAll();
    List<SupplierDTO> findActive();
    void delete(Long id);
}