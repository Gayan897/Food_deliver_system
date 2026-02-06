package com.fooddelivery.food_delivery.response;

import com.fooddelivery.food_delivery.model.USER_ROLE;
import lombok.Data;

@Data
public class AuthResponse {
    private String jwt;
    private String message;
    private USER_ROLE role;
}
