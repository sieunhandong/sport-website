package com.thanh_phoi_co.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI openAPI(@Value("${openapi.service.title}") String title,
                           @Value("${openapi.service.version}") String version,
                           @Value("${openapi.service.description}") String description,
                           @Value("${openapi.service.serverUrl}") String serverUrl){
        return new OpenAPI().info(new Info().title(title)
                .version(version).description(description)
                .license(new License().name("API license").url("http://dohu.vn/license")))
                .servers(List.of(new Server().url(serverUrl)));
    }

    @Bean
    public GroupedOpenApi publicApi(@Value("${openapi.service.api-docs}") String apiDocs){
        return GroupedOpenApi.builder()
                .group(apiDocs)
                .packagesToScan("com.thanh_phoi_co.controller")
                .build();
    }
}
