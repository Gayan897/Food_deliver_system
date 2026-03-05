package com.fooddelivery.food_delivery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class MenuItemRequest {
    @NotBlank(message = "Item name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    @NotBlank(message = "Category is required")
    private String category;

    private Boolean isVeg = true;
    private Boolean isAvailable = true;
    private String imageUrl;
    private Integer preparationTime;
    private Boolean isBestseller = false;
    private Integer discountPercentage = 0;
}
