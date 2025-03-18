package com.app85soft.qiqishop.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${system.backend.url}")
    private String BACKEND_URL;

    @Bean
    public GroupedOpenApi userGroupApi() {
        return GroupedOpenApi.builder()
                .group("user-api")
                .displayName("USER API")
                .pathsToMatch("/api/v*/**")
                .pathsToExclude("/api/vendor/v*/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminGroupApi() {
        return GroupedOpenApi.builder()
                .group("admin-api")
                .displayName("ADMIN API")
                .pathsToMatch("/api/admin/v*/**")
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()))
                .info(new Info().title("API karaoke")
                        .description("Api karaoke")
                        .version("1.0")
                        .contact(new Contact().name("ITS").email("info@its.com").url("https://interits.com/"))
                        .license(new License().name("License of API").url("https://interits.com/")))
                .addServersItem(new Server().url(BACKEND_URL).description("Generated server URL"));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
}
