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

                // PATIENTS
                .route("patient-service", r -> r
                        .path("/api/v1/patients/**")
                        .filters(f -> f.stripPrefix(2)
                        )
                        .uri("lb://PATIENT-SERVICE")
                )
                .route("patient-docs",r->r
                        .path("/docs/patients/**")
                        .filters(f->f.stripPrefix(2)
                        )
                        .uri("http://patient-service:8081")
                )

                // SECURITY
                .route("security-service", r -> r
                        .path("/api/v1/auth/**")
                                .filters(f -> f.stripPrefix(2)
                                )
                        .uri("lb://SECURITY-SERVICE")
                )

                .route("security-docs",r -> r
                        .path("/docs/security/**")
                        .filters(f -> f.stripPrefix(2)
                        )
                        .uri("http://security-service:8083")
                )

                // HISTORY
                .route("history-service", r -> r
                        .path("/api/v1/history/**")
                                .filters(f -> f.stripPrefix(2)
                                )
                        .uri("lb://HISTORY-SERVICE")
                )
                .route("history-docs",r->r
                        .path("/docs/history/**")
                        .filters(f->f.stripPrefix(2)
                        )
                        .uri("http://history-service:8084")
                )

                // EVALUATION
                .route("evaluation-service", r -> r
                        .path("/api/v1/evaluation/**")
                                .filters(f -> f.stripPrefix(2)
                                )
                        .uri("lb://EVALUATION-SERVICE")
                )

                .route("evaluation-docs",r->r
                        .path("/docs/evaluation/**")
                        .filters(f->f.stripPrefix(2))
                        .uri("http://evaluation-service:8085")
                )

                .route("swagger-config", r -> r
                        .path("/v3/api-docs/swagger-config")
                        .uri("lb://GATEWAY-SERVICE")
                )


                .build();

    }
}

