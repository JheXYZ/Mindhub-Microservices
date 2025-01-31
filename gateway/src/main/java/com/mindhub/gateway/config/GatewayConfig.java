package com.mindhub.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class GatewayConfig {

    private static final Logger log = LoggerFactory.getLogger(GatewayConfig.class);
    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private JwtRedirect jwtRedirect;

    private final Set<String>
            adminRole = Set.of(RoleType.ADMIN.toString()),
            userRole = Set.of(RoleType.USER.toString()),
            allRoles = Arrays.stream(RoleType.values())
                    .map(Enum::toString)
                    .collect(Collectors.toSet());

    private final Set<HttpMethod> allMethods = Set.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH, HttpMethod.DELETE);

    @Bean
    public RouteLocator customRouter(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route("auth", r -> r.path("/api/v1/auth/**")
                        .uri("lb://user-service"))

                .route("user-service", r -> r.path("/api/v1/user")
                        .filters(f -> f.filter( jwtRedirect.apply(new JwtRedirect.Config()))
                                .filter(jwtAuthFilter.apply(new JwtAuthenticationFilter.Config() {{
                                    setAllowedRoles(allRoles);
                                    setAllowedMethods(allMethods);
                                }})))
                        .uri("lb://user-service"))

                .route("user-service-admin", r -> r.path("/api/v1/users/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthenticationFilter.Config() {{
                            setAllowedRoles(adminRole);
                            setAllowedMethods(allMethods);
                        }})))
                        .uri("lb://user-service"))

                .route("product-service", r -> r.path("/api/v1/products/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthenticationFilter.Config() {{
                            setAllowedRoles(adminRole);
                            setAllowedMethods(allMethods);
                        }})))
                        .uri("lb://product-service"))

                .route("order-service", r -> r.path("/api/v1/user/orders", "/api/v1/user/orders/**")
                        .filters(f -> f.filter(jwtRedirect.apply(new JwtRedirect.Config()))
                                .filter(jwtAuthFilter.apply(new JwtAuthenticationFilter.Config() {{
                                    setAllowedRoles(allRoles);
                                    setAllowedMethods(allMethods);
                                }})))
                        .uri("lb://order-service"))

                .route("order-service", r -> r.path("/api/v1/orders/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthenticationFilter.Config() {{
                            setAllowedRoles(adminRole);
                            setAllowedMethods(allMethods);
                        }})))
                        .uri("lb://order-service"))

                .route("order-item-service", r -> r.path("/api/v1/order-items/**")
                        .filters(f -> f.filter(jwtAuthFilter.apply(new JwtAuthenticationFilter.Config() {{
                            setAllowedRoles(adminRole);
                            setAllowedMethods(allMethods);
                        }})))
                        .uri("lb://order-service"))
                .build();
    }

}
