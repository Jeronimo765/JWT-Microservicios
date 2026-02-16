package com.business.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private String orderNumber;
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    private String customerName;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private String status = "PENDING";
    private String shippingAddress;
    private String notes;
    private List<OrderDetailDTO> orderDetails = new ArrayList<>();
}