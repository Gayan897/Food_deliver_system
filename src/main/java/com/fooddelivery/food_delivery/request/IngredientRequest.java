package com.fooddelivery.food_delivery.request;

import lombok.Data;

@Data
public class IngredientRequest {

    private String name;
    private Long categoryId;
    private Long restaurantId;

}
