package com.codecool.stampcollection.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/api/**"))
                .apis(RequestHandlerSelectors.basePackage("com.codecool.stampcollection.controller"))
                .build()
                .tags(
                        new Tag("Stamps", "Endpoints for operations on stamps"),
                        new Tag("Transactions", "Endpoints for operations on transactions"),
                        new Tag("Denominations", "Endpoints for operations on denominations")
                )
                .apiInfo(apiDetails());
    }

    private ApiInfo apiDetails() {

        return new ApiInfo(
                "Stamp Collection",
                "Digitalized way to  record your stamp collection",
                "1.0",
                "Free",
                new Contact("xyz", "www.def.com", "xyz@def.com"),
                "licence",
                "www.def.com",
                Collections.emptyList()
                );
    }
}
