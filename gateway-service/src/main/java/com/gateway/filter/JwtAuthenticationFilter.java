package com.gateway.filter;

import com.gateway.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class JwtAuthenticationFilter implements GatewayFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Permitir rutas públicas (auth)
        String path = request.getURI().getPath();
        if (path.contains("/api/auth/")) {
            return chain.filter(exchange);
        }

        // Verificar si tiene el header Authorization
        if (!request.getHeaders().containsKey("Authorization")) {
            log.error("Missing Authorization header");
            return onError(exchange, HttpStatus.UNAUTHORIZED);
        }

        String authHeader = request.getHeaders().getFirst("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.error("Invalid Authorization header format");
            return onError(exchange, HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        try {
            if (!jwtUtil.validateToken(token)) {
                log.error("Invalid JWT token");
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            String username = jwtUtil.extractUsername(token);
            log.info("Valid token for user: {}", username);

            // Agregar el username al header para los microservicios
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .header("X-User-Name", username)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());

        } catch (Exception e) {
            log.error("JWT validation error: {}", e.getMessage());
            return onError(exchange, HttpStatus.UNAUTHORIZED);
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
}