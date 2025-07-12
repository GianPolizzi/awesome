package com.pizza.awesome;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Awesome Pizza API",
				version = "1.0.0",
				description = "API Order Management System for Awesome Pizza."
		),
		servers = {
				@Server(
						description = "Local Development Environent",
						url = "http://localhost:8080"
				)
		}
)
public class AwesomeApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwesomeApplication.class, args);
	}

}
