package com.example.lastproject.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI(@Value("${springdoc.version}") String springdocVersion) {
        Info info = new Info()
                .title("간단 커뮤니티")
                .version(springdocVersion)
                .description("간단한 카페느낌의 커뮤니티");

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("사용자") // API 그룹 이름
                .pathsToMatch("/user/**") // 문서화할 API 경로 패턴
                .build();
    }
    @Bean
    public GroupedOpenApi replyApi() {
        return GroupedOpenApi.builder()
                .group("댓글") // API 그룹 이름
                .pathsToMatch("/reply/**") // 문서화할 API 경로 패턴
                .build();

    }
    @Bean
    public GroupedOpenApi reReplyApi() {
        return GroupedOpenApi.builder()
                .group("대댓글") // API 그룹 이름
                .pathsToMatch("/NestedrReply/**") // 문서화할 API 경로 패턴
                .build();
    }
    @Bean
    public GroupedOpenApi boardApi() {
        return GroupedOpenApi.builder()
                .group("게시글") // API 그룹 이름
                .pathsToMatch("/board/**") // 문서화할 API 경로 패턴
                .build();
    }
}
