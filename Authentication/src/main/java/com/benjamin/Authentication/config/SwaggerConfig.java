package com.benjamin.Authentication.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;


import java.util.Collections;

@Configuration
public class SwaggerConfig {

    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Authentication API")
                        .version("1.0")
                        .description("API documentation for the authentication microservice"));
    }
}
