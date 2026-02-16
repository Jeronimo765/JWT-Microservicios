package com.business.controller;

import com.business.dto.OrderDetailDTO;
import com.business.service.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/order-details")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    @PostMapping
    public ResponseEntity<OrderDetailDTO> create(@Valid @RequestBody OrderDetailDTO dto) {
        try {
            OrderDetailDTO created = orderDetailService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailDTO> findById(@PathVariable Long id) {
        try {
            OrderDetailDTO detail = orderDetailService.findById(id);
            return ResponseEntity.ok(detail);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderDetailDTO>> findByOrder(@PathVariable Long orderId) {
        List<OrderDetailDTO> details = orderDetailService.findByOrder(orderId);
        return ResponseEntity.ok(details);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<OrderDetailDTO>> findByProduct(@PathVariable Long productId) {
        List<OrderDetailDTO> details = orderDetailService.findByProduct(productId);
        return ResponseEntity.ok(details);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            orderDetailService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Order Detail Controller is running");
    }
}