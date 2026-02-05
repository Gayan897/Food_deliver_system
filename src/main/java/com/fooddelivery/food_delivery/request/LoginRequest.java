package com.fooddelivery.food_delivery.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
