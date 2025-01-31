package com.mindhub.gateway.config;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtRedirect extends AbstractGatewayFilterFactory<JwtRedirect.Config> {

    @Autowired
    private JwtUtils jwtUtils;


    public JwtRedirect() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Obtener el token JWT del header
            String token = exchange.getRequest().getHeaders().getFirst("Authorization");

            if (token == null || !token.startsWith("Bearer "))
                return onError(exchange, "JWT token not found or invalid", HttpStatus.BAD_REQUEST);

            token = token.substring(7); // Eliminar "Bearer " del token
            if (jwtUtils.isTokenExpired(token))
                return onError(exchange, "JWT token is expired", HttpStatus.FORBIDDEN);

            String userId = null;

            userId = jwtUtils.extractId(token);

            if (userId == null)
                return onError(exchange, "user not found", HttpStatus.NOT_FOUND);

            // pathSize = 6: "", 8: "orders", 9: "{id}, 12: "confirm"
            int pathSize = exchange.getRequest().getPath().elements().size();
            ServerHttpRequest modifiedRequest;
            // this switch redirects to the correct service
            switch (pathSize) {
                case 6 -> {
                    modifiedRequest = exchange.getRequest()
                            .mutate()
                            .path("/api/v1/users/" + userId) // Nueva ruta
                            .build();
                }
                case 8 -> {
                    if (!exchange.getRequest().getPath().subPath(7, 8).toString().equals("orders"))
                        return onError(exchange, "invalid path", HttpStatus.BAD_REQUEST);
                    modifiedRequest = exchange.getRequest()
                            .mutate()
                            .path("/api/v1/orders?userId=" + userId) // Nueva ruta
                            .build();
                }
                case 10 -> {
                    String orderId = exchange.getRequest().getPath().elements().get(9).value();
                    if (!exchange.getRequest().getPath().subPath(7, 8).toString().equals("orders") || !orderId.matches("\\d+"))
                        return onError(exchange, "invalid path", HttpStatus.BAD_REQUEST);
                    modifiedRequest = exchange.getRequest()
                            .mutate()
                            .path("/api/v1/orders/" + orderId + "?userId=" + userId)
                            .build();
                }
                case 12 -> {
                    String orderId = exchange.getRequest().getPath().elements().get(9).value();
                    if (!exchange.getRequest().getPath().subPath(7, 8).toString().equals("orders") || !exchange.getRequest().getPath().subPath(11).toString().equals("confirm")|| !orderId.matches("\\d+"))
                        return onError(exchange, "invalid path", HttpStatus.BAD_REQUEST);
                    modifiedRequest = exchange.getRequest()
                            .mutate()
                            .path("/api/v1/orders/" + orderId  + "/confirm" + "?userId=" + userId)
                            .build();
                }
                default -> {
                    return onError(exchange, "invalid path", HttpStatus.BAD_REQUEST);
                }
            }
            ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedRequest).build();
            return chain.filter(modifiedExchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        exchange.getResponse().getHeaders().set("error", err);
        return exchange.getResponse().setComplete();
    }
    
    public static class Config {
    }
}
