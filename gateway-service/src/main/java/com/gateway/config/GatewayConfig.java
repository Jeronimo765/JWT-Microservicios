package com.gateway.config;

import com.gateway.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Ruta para Auth Service - Sin autenticación
                .route("auth-service", r -> r
                        .path("/api/auth/**")
                        .uri("http://localhost:8081"))
                
                // Rutas para Business Service - Con autenticación JWT
                .route("business-categories", r -> r
                        .path("/api/business/categories/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("http://localhost:8082"))
                
                .route("business-suppliers", r -> r
                        .path("/api/business/suppliers/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("http://localhost:8082"))
                
                .route("business-products", r -> r
                        .path("/api/business/products/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("http://localhost:8082"))
                
                .route("business-customers", r -> r
                        .path("/api/business/customers/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("http://localhost:8082"))
                
                .route("business-orders", r -> r
                        .path("/api/business/orders/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("http://localhost:8082"))
                
                .route("business-order-details", r -> r
                        .path("/api/business/order-details/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("http://localhost:8082"))
                
                .build();
    }
}