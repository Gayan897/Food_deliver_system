package com.fooddelivery.food_delivery.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long userId;
    private String email;
    private String name;
    private String role;

    public JwtResponse(String token, Long userId, String email, String name, String role) {
        this.token = token;
        this.type = "Bearer";
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.role = role;
    }
}
