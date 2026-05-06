package com.Medilabo_solutions.Gateway_service.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRouteConfig {
    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("patient-service", r -> r
                        .path("/api/v1/patients/**")
                        .filters(f -> f
                                .rewritePath("/api/v1/patients/(?<segment>.*)", "/patients/${segment}")
                        )
                        .uri("lb://PATIENT-SERVICE")
                )
                .build();
    }
}

