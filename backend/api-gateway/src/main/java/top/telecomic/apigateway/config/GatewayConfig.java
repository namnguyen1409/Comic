package top.telecomic.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/auth/**")
                        .uri("lb://auth-service"))
                .route("media-service", r -> r.path("/media/**")
                        .uri("lb://media-service"))
                .route("comic-service", r -> r.path("/comic/**")
                        .uri("lb://comic-service"))
                .route("user-service", r -> r.path("/user/**")
                        .uri("lb://user-service"))
                .build();
    }

}
