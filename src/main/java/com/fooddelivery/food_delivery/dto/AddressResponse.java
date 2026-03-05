package com.fooddelivery.food_delivery.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {
    private Long id;
    private String addressLine;
    private String city;
    private String postalCode;
    private String state;
    private String landmark;
    private Boolean isDefault;
    private String type;
    private LocalDateTime createdAt;
}
