package org.example.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "My Restful Service API 명세서",
                description = "SpringBoot로 개발하는 RESTfulAPI 명세서 입니다.",
                version = "v1.0.0")
)
@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi customTestOpenAPI() {
        String[] paths = {"/api/users/**", "/logout"};

        return GroupedOpenApi.builder()
                .group("사용자를 위한 API")
                .pathsToMatch(paths)
                .build();
    }

}
