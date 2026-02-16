package com.business.service.impl;

import com.business.dto.CustomerDTO;
import com.business.entity.Customer;
import com.business.repository.CustomerRepository;
import com.business.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public CustomerDTO create(CustomerDTO dto) {
        if (customerRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Customer with email '" + dto.getEmail() + "' already exists");
        }

        Customer customer = new Customer();
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());
        customer.setCity(dto.getCity());
        customer.setCountry(dto.getCountry());
        customer.setPostalCode(dto.getPostalCode());
        customer.setIsActive(dto.getIsActive());

        Customer saved = customerRepository.save(customer);
        return toDTO(saved);
    }

    @Override
    @Transactional
    public CustomerDTO update(Long id, CustomerDTO dto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        if (!customer.getEmail().equals(dto.getEmail()) && customerRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Customer with email '" + dto.getEmail() + "' already exists");
        }

        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());
        customer.setCity(dto.getCity());
        customer.setCountry(dto.getCountry());
        customer.setPostalCode(dto.getPostalCode());
        customer.setIsActive(dto.getIsActive());

        Customer updated = customerRepository.save(customer);
        return toDTO(updated);
    }

    @Override
    public CustomerDTO findById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        return toDTO(customer);
    }

    @Override
    public List<CustomerDTO> findAll() {
        return customerRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerDTO> findActive() {
        return customerRepository.findByIsActiveTrue().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    private CustomerDTO toDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhone());
        dto.setAddress(customer.getAddress());
        dto.setCity(customer.getCity());
        dto.setCountry(customer.getCountry());
        dto.setPostalCode(customer.getPostalCode());
        dto.setIsActive(customer.getIsActive());
        return dto;
    }
}