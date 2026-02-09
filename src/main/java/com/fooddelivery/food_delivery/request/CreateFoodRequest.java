package com.fooddelivery.food_delivery.request;

import com.fooddelivery.food_delivery.model.Category;
import com.fooddelivery.food_delivery.model.IngredientsItem;
import lombok.Data;

import java.util.List;

@Data
public class CreateFoodRequest {

    private String name;
    private String description;
    private  long price;

    private Category category;
    private List<String> images;

    private Long restaurantId;
    private boolean vegetarian;
    private boolean seasonal;
    private List<IngredientsItem> ingredients;
}
