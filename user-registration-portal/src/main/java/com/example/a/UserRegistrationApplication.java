package com.example.a;

import com.example.a.service.ApiLogConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class UserRegistrationApplication implements CommandLineRunner {

	@Autowired
	private ApiLogConsumerService apiLogConsumerService;

	public static void main(String[] args) {
		SpringApplication.run(UserRegistrationApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Override
	public void run(String... args) throws Exception {
		apiLogConsumerService.consume();
	}
}
