package com.business.service.impl;

import com.business.dto.OrderDetailDTO;
import com.business.entity.Order;
import com.business.entity.OrderDetail;
import com.business.entity.Product;
import com.business.repository.OrderDetailRepository;
import com.business.repository.OrderRepository;
import com.business.repository.ProductRepository;
import com.business.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OrderDetailDTO create(OrderDetailDTO dto) {
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + dto.getOrderId()));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + dto.getProductId()));

        OrderDetail detail = new OrderDetail();
        detail.setOrder(order);
        detail.setProduct(product);
        detail.setQuantity(dto.getQuantity());
        detail.setUnitPrice(dto.getUnitPrice() != null ? dto.getUnitPrice() : product.getPrice());

        OrderDetail saved = orderDetailRepository.save(detail);
        return toDTO(saved);
    }

    @Override
    public OrderDetailDTO findById(Long id) {
        OrderDetail detail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order detail not found with id: " + id));
        return toDTO(detail);
    }

    @Override
    public List<OrderDetailDTO> findByOrder(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDetailDTO> findByProduct(Long productId) {
        return orderDetailRepository.findByProductId(productId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!orderDetailRepository.existsById(id)) {
            throw new RuntimeException("Order detail not found with id: " + id);
        }
        orderDetailRepository.deleteById(id);
    }

    private OrderDetailDTO toDTO(OrderDetail detail) {
        OrderDetailDTO dto = new OrderDetailDTO();
        dto.setId(detail.getId());
        dto.setOrderId(detail.getOrder().getId());
        dto.setProductId(detail.getProduct().getId());
        dto.setProductName(detail.getProduct().getName());
        dto.setQuantity(detail.getQuantity());
        dto.setUnitPrice(detail.getUnitPrice());
        dto.setSubtotal(detail.getSubtotal());
        return dto;
    }
}