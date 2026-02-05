package com.fooddelivery.food_delivery.response;

import com.fooddelivery.food_delivery.model.USER_ROLE;

@Data
public class AuthResponse {
    private String jwt;
    private String message;
    private USER_ROLE role;
}
