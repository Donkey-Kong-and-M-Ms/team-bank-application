package com.cognixia.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com")
@ComponentScan(basePackages = "com")
@EntityScan(basePackages = "com.cognixia.application.model")
@EnableJpaRepositories("com.cognixia.application.repository")
public class TeamlibraryapplicationApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeamlibraryapplicationApplication.class, args);
	}

}
