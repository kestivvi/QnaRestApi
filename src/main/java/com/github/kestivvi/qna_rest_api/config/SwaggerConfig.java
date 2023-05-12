package com.github.kestivvi.qna_rest_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Q&A REST API", version = "${app.version}", description = "Q&A REST API documentation"))
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }

    @Bean
    public GroupedOpenApi allOpenApi() {
        String[] packages = {"com.github.kestivvi.qna_rest_api"};
        return GroupedOpenApi.builder().group("all").packagesToScan(packages).build();
    }

    @Bean
    public GroupedOpenApi questionOpenApi() {
        String[] packages = {"com.github.kestivvi.qna_rest_api.domain.question"};
        return GroupedOpenApi.builder().group("question").packagesToScan(packages).build();
    }

    @Bean
    public GroupedOpenApi authOpenApi() {
        String[] packages = {"com.github.kestivvi.qna_rest_api.auth"};
        return GroupedOpenApi.builder().group("auth").packagesToScan(packages).build();
    }

    @Bean
    public GroupedOpenApi usersOpenApi() {
        String[] packages = {"com.github.kestivvi.qna_rest_api.domain.user"};
        return GroupedOpenApi.builder().group("users").packagesToScan(packages).build();
    }

}
