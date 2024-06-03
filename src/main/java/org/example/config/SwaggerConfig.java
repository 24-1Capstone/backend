package org.example.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "My Restful Service API 명세서",
                description = "SpringBoot로 개발하는 RESTfulAPI 명세서 입니다." +
                        "  \n[깃허브 로그인](https://github.com/login)  \n[깃허브 로그아웃](https://github.com/logout)",
                version = "v1.0.0"),
        security = {@SecurityRequirement(name = "Authorization")}
)

@SecuritySchemes({
        @SecurityScheme(
                name = "Authorization",
                type = SecuritySchemeType.HTTP,
                scheme = "bearer",
                bearerFormat = "JWT"
        )
})
@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi customTestOpenAPI() {
        String[] paths = {"/api/user/**", "/logout", "/api/user/**", "/api/users/**", "/api/meetings/**", "/api/reservation/**", "/api/token"};

        return GroupedOpenApi.builder()
                .group("사용자를 위한 API")
                .pathsToMatch(paths)
                .build();
    }

}
