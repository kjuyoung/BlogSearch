package com.young.blogsearch.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI().info(new Info().title("Blogsearch API")
                                            .description("블로그 검색 서비스 API 명세서입니다.")
                                            .version("v1"));
    }
}
