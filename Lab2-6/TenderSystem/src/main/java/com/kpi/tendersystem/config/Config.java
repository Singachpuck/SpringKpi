package com.kpi.tendersystem.config;

import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Tender System REST Api documentation",
                version = "v1",
                contact = @Contact(
                        name = "Team 1",
                        url = "http://localhost:8080/"
                ),
                description = "REST Api that allows you to interact with Tender System application"
        )
)
@Configuration
public class Config {

    @Bean
    public Gson gson() {
        return new Gson();
    }
}
