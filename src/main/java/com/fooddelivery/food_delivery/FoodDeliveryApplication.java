package com.fooddelivery.food_delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class FoodDeliveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodDeliveryApplication.class, args);
	}
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
