package com.waffiyyi.bookmarketplace.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition(info = @Info(title = "Book MarketPlace API", version = "1.0.0",
                                description = "Book MarketPlace using RESTful API",
                                termsOfService = "Waffiyyi",
                                contact = @Contact(name = "Mr Waffiyyi",
                                                   email = "fasholawafiyyi@gmail.com"),
                                license = @License(name = "licence", url = "waffiyyi")

),
                   servers = {
                      @Server(url = "https://book-market-place-backend.onrender.com",
                              description = "Production Server"),

                      @Server(url = "http://localhost:8015",
                              description = "Local Server")
                   },
                   security = {@io.swagger.v3.oas.annotations.security.SecurityRequirement(
                      name = "bearerAuth", scopes = {"read", "write"})})
@Configuration
public class SwaggerConfig {
  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI().addSecurityItem(
       new SecurityRequirement().addList("bearerAuth")).components(
       new Components().addSecuritySchemes("bearerAuth", new SecurityScheme().type(
          SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
  }
}