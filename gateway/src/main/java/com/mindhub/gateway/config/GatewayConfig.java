package com.mindhub.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouter(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route("user-service", r -> r.path("/api/v1/users/**")
                        .uri("lb://user-service"))
                .route("product-service", r -> r.path("/api/v1/products/**")
                        .uri("lb://product-service"))
                .route("order-service", r -> r.path("/api/v1/orders/**")
                        .uri("lb://order-service"))
                .route("order-item-service", r -> r.path("/api/v1/order-items/**")
                        .uri("lb://order-service"))
                .build();
    }
}
