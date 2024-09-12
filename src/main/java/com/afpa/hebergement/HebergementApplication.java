package com.afpa.hebergement;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class HebergementApplication {

	public static void main(String[] args) {

		// Charger les variables d'environnement à partir du fichier .env
		Dotenv dotenv = Dotenv.configure().load();

		System.setProperty("JDBC_URL", dotenv.get("JDBC_URL"));
		System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

		System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
		System.setProperty("JWT_EXPIRATION", dotenv.get("JWT_EXPIRATION"));

		// lancement de l'application
		SpringApplication.run(HebergementApplication.class, args);

	}

}
