package com.catalogo.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("Catalogo API")
                .version("v1")
                .description("API catalogo prodotti con JWT, cache Redis, ordini e Swagger UI"));
    }
}
