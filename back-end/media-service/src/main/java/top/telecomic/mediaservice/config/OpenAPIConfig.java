package top.telecomic.mediaservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("X-User-Id", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("X-User-Id")
                                .description("User ID of the authenticated user")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("X-User-Id"))
                .servers(
                        List.of(
                                new Server().url("http://localhost:8765")
                                        .description("Local Development Server"),
                                new Server().url("https://media-service.telecomic.top")
                                        .description("Production Server")
                        )
                )
                .info(new Info()
                        .title("Media Service API")
                        .version("v1")
                        .description("API documentation for the Media Service, handling media files and related operations.")
                );
    }
}
