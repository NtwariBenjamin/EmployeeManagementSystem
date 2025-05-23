package com.benjamin.Employee_Service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Employee API")
                        .version("1.0")
                        .description("API documentation for the Employee microservice"));
    }
}
