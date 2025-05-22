package com.example.ReviewEngine;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class ReviewEngineApplication {

	public static void main(String[] args) {
		if (!"prod".equalsIgnoreCase(System.getenv("SPRING_PROFILES_ACTIVE"))) {
			Dotenv dotenv = Dotenv.configure()
					.directory("C:/git/inlämning/grupparbete/ReviewEngine")
					.ignoreIfMissing() // Ignorera om .env saknas
					.load();
			dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		}
		SpringApplication.run(ReviewEngineApplication.class, args);
	}
}

