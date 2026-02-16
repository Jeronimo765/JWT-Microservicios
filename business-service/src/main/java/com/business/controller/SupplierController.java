package com.business.controller;

import com.business.dto.SupplierDTO;
import com.business.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping
    public ResponseEntity<SupplierDTO> create(@Valid @RequestBody SupplierDTO dto) {
        try {
            SupplierDTO created = supplierService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierDTO> update(@PathVariable Long id, @Valid @RequestBody SupplierDTO dto) {
        try {
            SupplierDTO updated = supplierService.update(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierDTO> findById(@PathVariable Long id) {
        try {
            SupplierDTO supplier = supplierService.findById(id);
            return ResponseEntity.ok(supplier);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<SupplierDTO>> findAll() {
        List<SupplierDTO> suppliers = supplierService.findAll();
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/active")
    public ResponseEntity<List<SupplierDTO>> findActive() {
        List<SupplierDTO> suppliers = supplierService.findActive();
        return ResponseEntity.ok(suppliers);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            supplierService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Supplier Controller is running");
    }
}