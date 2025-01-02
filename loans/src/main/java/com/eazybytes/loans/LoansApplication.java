package com.eazybytes.loans;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(
        info = @Info(title = "Loans MS Rest API documentation",
                description = "EazyBank Loans microservice Rest API documentation",
                version = "v1",
                contact = @Contact(
                        name = "Jayant Kumar",
                        email = "jayant.tjr@gmail.com"
                ),
                license = @License(
                        name = "Apache 2.0"
                )))
public class LoansApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoansApplication.class, args);
    }

}
