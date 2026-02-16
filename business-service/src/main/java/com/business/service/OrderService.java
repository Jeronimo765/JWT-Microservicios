package com.business.service;

import com.business.dto.OrderDTO;
import com.business.entity.Order;
import java.util.List;

public interface OrderService {
    OrderDTO create(OrderDTO dto);
    OrderDTO update(Long id, OrderDTO dto);
    OrderDTO findById(Long id);
    List<OrderDTO> findAll();
    List<OrderDTO> findByCustomer(Long customerId);
    List<OrderDTO> findByStatus(Order.OrderStatus status);
    OrderDTO updateStatus(Long id, Order.OrderStatus status);
    void delete(Long id);
}