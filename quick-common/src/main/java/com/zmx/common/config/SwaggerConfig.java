package com.zmx.common.config;

import com.zmx.common.properties.JwtAdminProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger配置类
 */
@Configuration
public class SwaggerConfig {

    /**
     * 用户相关接口分组
     */
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("后台管理接口")
                .packagesToScan("com.zmx.quickserver.controller.admin")
                .build();
    }

    /**
     * 订单相关接口分组
     */
    @Bean
    public GroupedOpenApi orderApi() {
        return GroupedOpenApi.builder()
                .group("用户接口")
                .packagesToScan("com.zmx.quickserver.controller.user")
                .build();
    }

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
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}