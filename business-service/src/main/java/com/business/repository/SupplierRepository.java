package com.business.repository;

import com.business.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findByEmail(String email);
    List<Supplier> findByIsActiveTrue();
    List<Supplier> findByCountry(String country);
    Boolean existsByEmail(String email);
}