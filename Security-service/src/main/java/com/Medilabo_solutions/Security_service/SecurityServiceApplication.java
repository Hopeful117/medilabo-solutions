package com.Medilabo_solutions.Security_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SecurityServiceApplication {

	public static void main(String[] args) {


        SpringApplication.run(SecurityServiceApplication.class, args);
	}

}
