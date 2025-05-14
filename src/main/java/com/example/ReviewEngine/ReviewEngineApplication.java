package com.example.ReviewEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class ReviewEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewEngineApplication.class, args);
	}
}

