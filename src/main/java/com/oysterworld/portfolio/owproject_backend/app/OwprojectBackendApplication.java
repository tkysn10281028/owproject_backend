package com.oysterworld.portfolio.owproject_backend.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.oysterworld.portfolio.owproject_backend")
public class OwprojectBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(OwprojectBackendApplication.class, args);
	}

}
