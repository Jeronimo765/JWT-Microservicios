package com.business.repository;

import com.business.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    List<Customer> findByIsActiveTrue();
    List<Customer> findByCity(String city);
    List<Customer> findByCountry(String country);
    Boolean existsByEmail(String email);
}