package com.fooddelivery.food_delivery.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressRequest {
    @NotBlank(message = "Address line is required")
    private String addressLine;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Postal code is required")
    private String postalCode;

    private String state;
    private String landmark;
    private Boolean isDefault = false;
    private String type = "HOME";
}
