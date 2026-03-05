package com.fooddelivery.food_delivery.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddToCartRequest {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Menu item ID is required")
    private Long menuItemId;

    @NotNull(message = "Menu item name is required")
    private String menuItemName;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be positive")
    private Double price;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity = 1;

    private String customizations;

    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;

    private String restaurantName;
}
