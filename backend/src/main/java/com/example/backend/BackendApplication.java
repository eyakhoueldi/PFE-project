package com.example.backend;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableMongoRepositories("com.example.backend.repositories")  // ✅ Ensures MongoDB repositories are scanned
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
