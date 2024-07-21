package com.kafkab2c.KafkaB2C;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * KafkaB2CApplication class serves as the entry point for the Spring Boot application.
 * It also enables scheduling for periodic tasks.
 */
@SpringBootApplication
@EnableScheduling
public class KafkaB2CApplication {

	/**
	 * Main method to start the Spring Boot application.
	 *
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(KafkaB2CApplication.class, args);
	}
}
