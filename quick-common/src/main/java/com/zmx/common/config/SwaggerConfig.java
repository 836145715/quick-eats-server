package com.zmx.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger配置类
 */
@Configuration
public class SwaggerConfig {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Quick Eats API")
                        .description("餐急送外卖平台API文档")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Quick Eats Team")
                                .email("contact@quick-eats.com")
                                .url("https://www.quick-eats.com"))
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")))
                // 添加JWT认证配置
                .addSecurityItem(new SecurityRequirement().addList(tokenHeader))
                .components(new Components()
                        .addSecuritySchemes(tokenHeader, new SecurityScheme()
                                .name(tokenHeader)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .description("请输入JWT令牌")));
    }
}