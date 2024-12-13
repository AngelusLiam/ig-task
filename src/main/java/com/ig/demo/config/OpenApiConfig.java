package com.ig.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.config.Elements.JWT;
import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;

@Configuration
public class OpenApiConfig {

    SecurityScheme bearerScheme = new SecurityScheme()
            .name(AUTHORIZATION)
            .type(SecurityScheme.Type.HTTP)  // HTTP schema per il Bearer Token
            .scheme(BEARER.getValue())                // Tipo di schema 'bearer'
            .bearerFormat(JWT);


    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                .title("API microservizio Ig-task")
                .version("1.0")
                .description("API microservizio per la demo task di IG"))
                .components(new Components()
                        .addSecuritySchemes(AUTHORIZATION, bearerScheme))
                .addSecurityItem(new SecurityRequirement().addList(AUTHORIZATION));
    }
}
