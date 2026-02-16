package com.business.service.impl;

import com.business.dto.SupplierDTO;
import com.business.entity.Supplier;
import com.business.repository.SupplierRepository;
import com.business.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    @Override
    @Transactional
    public SupplierDTO create(SupplierDTO dto) {
        if (dto.getEmail() != null && supplierRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Supplier with email '" + dto.getEmail() + "' already exists");
        }

        Supplier supplier = new Supplier();
        supplier.setName(dto.getName());
        supplier.setContactName(dto.getContactName());
        supplier.setEmail(dto.getEmail());
        supplier.setPhone(dto.getPhone());
        supplier.setAddress(dto.getAddress());
        supplier.setCity(dto.getCity());
        supplier.setCountry(dto.getCountry());
        supplier.setIsActive(dto.getIsActive());

        Supplier saved = supplierRepository.save(supplier);
        return toDTO(saved);
    }

    @Override
    @Transactional
    public SupplierDTO update(Long id, SupplierDTO dto) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));

        if (dto.getEmail() != null && !dto.getEmail().equals(supplier.getEmail()) 
                && supplierRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Supplier with email '" + dto.getEmail() + "' already exists");
        }

        supplier.setName(dto.getName());
        supplier.setContactName(dto.getContactName());
        supplier.setEmail(dto.getEmail());
        supplier.setPhone(dto.getPhone());
        supplier.setAddress(dto.getAddress());
        supplier.setCity(dto.getCity());
        supplier.setCountry(dto.getCountry());
        supplier.setIsActive(dto.getIsActive());

        Supplier updated = supplierRepository.save(supplier);
        return toDTO(updated);
    }

    @Override
    public SupplierDTO findById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));
        return toDTO(supplier);
    }

    @Override
    public List<SupplierDTO> findAll() {
        return supplierRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SupplierDTO> findActive() {
        return supplierRepository.findByIsActiveTrue().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new RuntimeException("Supplier not found with id: " + id);
        }
        supplierRepository.deleteById(id);
    }

    private SupplierDTO toDTO(Supplier supplier) {
        SupplierDTO dto = new SupplierDTO();
        dto.setId(supplier.getId());
        dto.setName(supplier.getName());
        dto.setContactName(supplier.getContactName());
        dto.setEmail(supplier.getEmail());
        dto.setPhone(supplier.getPhone());
        dto.setAddress(supplier.getAddress());
        dto.setCity(supplier.getCity());
        dto.setCountry(supplier.getCountry());
        dto.setIsActive(supplier.getIsActive());
        return dto;
    }
}