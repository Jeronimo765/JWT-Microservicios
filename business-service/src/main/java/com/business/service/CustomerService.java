package com.business.service;

import com.business.dto.CustomerDTO;
import java.util.List;

public interface CustomerService {
    CustomerDTO create(CustomerDTO dto);
    CustomerDTO update(Long id, CustomerDTO dto);
    CustomerDTO findById(Long id);
    List<CustomerDTO> findAll();
    List<CustomerDTO> findActive();
    void delete(Long id);
}