package com.fooddelivery.food_delivery.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalTime;

@Data
public class RestaurantRequest {
    @NotBlank(message = "Restaurant name is required")
    private String name;

    @NotBlank(message = "Cuisine is required")
    private String cuisine;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone is required")
    private String phone;

    private LocalTime openingTime;
    private LocalTime closingTime;
    private String imageUrl;
    private String description;
    private String deliveryTime;
    private Double minOrderAmount;
}
