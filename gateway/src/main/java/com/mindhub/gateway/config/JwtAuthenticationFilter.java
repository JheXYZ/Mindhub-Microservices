package com.mindhub.gateway.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Set;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    @Autowired
    private JwtUtils jwtUtils;

    public JwtAuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);

                if (!jwtUtils.isTokenExpired(token)) {
                    if (isAllowed(token, exchange, config))
                        return chain.filter(exchange);
                    else {
                        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                        return exchange.getResponse().setComplete();
                    }
                }
            }

            // Si el token no es válido o no está presente, devolver un error 401
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        };
    }

    private boolean isAllowed(String token, ServerWebExchange exchange, Config config){
        return hasRequiredRole( token, config.getAllowedRoles() ) && isAllowedMethod( exchange.getRequest().getMethod(), config.getAllowedMethods() );
    }

    private boolean hasRequiredRole(String token, Set<String> allowedRoles) {
        return allowedRoles.contains(jwtUtils.extractRole(token));
    }

    private boolean isAllowedMethod(HttpMethod method, Set<HttpMethod> allowedMethods) {
        return allowedMethods.contains(method);
    }

    public static class Config {
        private Set<String> allowedRoles;

        private Set<HttpMethod> allowedMethods;

        public Set<String> getAllowedRoles() {
            return allowedRoles;
        }

        public void setAllowedRoles(Set<String> requiredRole) {
            this.allowedRoles = requiredRole;
        }

        public void setAllowedMethods(Set<HttpMethod> allowedMethods) {
            this.allowedMethods = allowedMethods;
        }

        public Set<HttpMethod> getAllowedMethods() {
            return allowedMethods;
        }
    }
}

