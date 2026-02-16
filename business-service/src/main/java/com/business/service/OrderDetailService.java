package com.business.service;

import com.business.dto.OrderDetailDTO;
import java.util.List;

public interface OrderDetailService {
    OrderDetailDTO create(OrderDetailDTO dto);
    OrderDetailDTO findById(Long id);
    List<OrderDetailDTO> findByOrder(Long orderId);
    List<OrderDetailDTO> findByProduct(Long productId);
    void delete(Long id);
}