package com.qqxhb.prediction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableNeo4jRepositories("com.qqxhb.prediction.repository")
public class PreDictionApplication {
	public static void main(String[] args) {
		SpringApplication.run(PreDictionApplication.class, args);
	}

}