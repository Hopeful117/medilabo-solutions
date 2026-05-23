package com.Medilabo_solutions.Gateway_service.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * GatewayRouteConfig is a configuration class that defines the routing rules for the API Gateway.
 * It uses Spring Cloud Gateway to route incoming requests to the appropriate microservices based on the URL path.
 * The customRoutes method defines two routes: one for the Patient Service and another for the
 */
@Configuration
public class GatewayRouteConfig {
    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("patient-service", r -> r
                        .path("/api/v1/patients/**")
                        .filters(f -> f.stripPrefix(2)

                        )
                        .uri("lb://PATIENT-SERVICE")
                )
                .route("security-service", r -> r
                        .path("/api/v1/auth/**")
                                .filters(f -> f.stripPrefix(2))
                        .uri("lb://SECURITY-SERVICE")
                )
                .route("history-service", r -> r
                        .path("/api/v1/history/**")
                                .filters(f -> f.stripPrefix(2))
                        .uri("lb://HISTORY-SERVICE")
                )
                .route("evaluation-service", r -> r
                        .path("/api/v1/evaluation/**")
                                .filters(f -> f.stripPrefix(2))
                        .uri("lb://EVALUATION-SERVICE")
                )


                .build();

    }
}

