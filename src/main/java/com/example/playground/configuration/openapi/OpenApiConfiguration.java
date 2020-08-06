package com.example.playground.configuration.openapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Playground API",
                version = "0.0.1-SNAPSHOT",
                description = "Pet project to try-out technologies, libraries",
                contact = @Contact(
                        email = "istvan.bartuszek@gmail.com"
                ),
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"
                )
        )
)
@Configuration
public class OpenApiConfiguration {

}
