package br.com.joaocastro.projeto_livraria_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI livrariaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Livraria")
                        .description("API REST para gerenciamento de livros")
                        .version("2.0.0"));
    }
}

