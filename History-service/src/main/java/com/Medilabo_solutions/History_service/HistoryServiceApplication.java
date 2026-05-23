package com.Medilabo_solutions.History_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HistoryServiceApplication {

	public static void main(String[] args) {
        // Charger les variables du fichier .env avant de lancer Spring Boot
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );
        SpringApplication.run(HistoryServiceApplication.class, args);
	}

}
