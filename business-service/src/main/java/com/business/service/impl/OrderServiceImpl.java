package com.business.service.impl;

import com.business.dto.OrderDTO;
import com.business.dto.OrderDetailDTO;
import com.business.entity.Customer;
import com.business.entity.Order;
import com.business.entity.OrderDetail;
import com.business.entity.Product;
import com.business.repository.CustomerRepository;
import com.business.repository.OrderDetailRepository;
import com.business.repository.OrderRepository;
import com.business.repository.ProductRepository;
import com.business.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + dto.getCustomerId()));

        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.valueOf(dto.getStatus()));
        order.setShippingAddress(dto.getShippingAddress());
        order.setNotes(dto.getNotes());

        Order savedOrder = orderRepository.save(order);

        BigDecimal totalAmount = BigDecimal.ZERO;
        if (dto.getOrderDetails() != null && !dto.getOrderDetails().isEmpty()) {
            for (OrderDetailDTO detailDTO : dto.getOrderDetails()) {
                Product product = productRepository.findById(detailDTO.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found with id: " + detailDTO.getProductId()));

                OrderDetail detail = new OrderDetail();
                detail.setOrder(savedOrder);
                detail.setProduct(product);
                detail.setQuantity(detailDTO.getQuantity());
                detail.setUnitPrice(product.getPrice());
                
                orderDetailRepository.save(detail);
                totalAmount = totalAmount.add(detail.getSubtotal());
            }
        }

        savedOrder.setTotalAmount(totalAmount);
        savedOrder = orderRepository.save(savedOrder);

        return toDTO(savedOrder);
    }

    @Override
    @Transactional
    public OrderDTO update(Long id, OrderDTO dto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        if (dto.getCustomerId() != null && !dto.getCustomerId().equals(order.getCustomer().getId())) {
            Customer customer = customerRepository.findById(dto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found with id: " + dto.getCustomerId()));
            order.setCustomer(customer);
        }

        if (dto.getStatus() != null) {
            order.setStatus(Order.OrderStatus.valueOf(dto.getStatus()));
        }

        order.setShippingAddress(dto.getShippingAddress());
        order.setNotes(dto.getNotes());

        Order updated = orderRepository.save(order);
        return toDTO(updated);
    }

    @Override
    public OrderDTO findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return toDTO(order);
    }

    @Override
    public List<OrderDTO> findAll() {
        return orderRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> findByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> findByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderDTO updateStatus(Long id, Order.OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        
        order.setStatus(status);
        Order updated = orderRepository.save(order);
        return toDTO(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private OrderDTO toDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setCustomerId(order.getCustomer().getId());
        dto.setCustomerName(order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus().name());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setNotes(order.getNotes());

        List<OrderDetailDTO> details = orderDetailRepository.findByOrderId(order.getId()).stream()
                .map(this::toDetailDTO)
                .collect(Collectors.toList());
        dto.setOrderDetails(details);

        return dto;
    }

    private OrderDetailDTO toDetailDTO(OrderDetail detail) {
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