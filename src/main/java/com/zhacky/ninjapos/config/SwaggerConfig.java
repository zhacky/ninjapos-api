package com.zhacky.ninjapos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.singletonList;

@EnableSwagger2
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket ninjaAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zhacky.ninjapos"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaInfo())
                .securitySchemes(singletonList(userToken()))
                .securityContexts(singletonList(securityContext()));
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .forPaths(PathSelectors.regex("/orders"))
                .forPaths(PathSelectors.regex("/products"))
                .build();
    }

    private ApiKey userToken() {
        return new ApiKey("JWT","Authorization","header");
    }

    private ApiInfo metaInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "Ninja POS Restful API Service",
                "Restful API endpoints for Ninja POS",
                "1.0.0",
                "Terms of Service",
                contactInfo(),
                "Proprietary License",
                "https://www.apache.org/license.html",
                newArrayList()
        );
        return apiInfo;
    }

    private Contact contactInfo() {
        return new Contact(
                "Zhack Ariya",
                "zhacky.com",
                "zhackariya@gmail.com"
        );
    }
}
