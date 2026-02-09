package com.fooddelivery.food_delivery.service;

import com.fooddelivery.food_delivery.model.Category;
import com.fooddelivery.food_delivery.model.Food;
import com.fooddelivery.food_delivery.model.Restaurant;
import com.fooddelivery.food_delivery.request.CreateFoodRequest;

import java.util.List;

public interface FoodService {

    public Food createFood(CreateFoodRequest req, Category category, Restaurant restaurant);

    void deleteFood(Long foodId) throws Exception;

    public List<Food> getRestaurantsFood(Long restaurantId, boolean isVegetarian, boolean isNonveg, boolean isSeasonal, String foodCategory);

    public List<Food> searchFood(String keyword);

    public Food findFoodById(Long foodId)throws Exception;

    public Food updateAvailabilityStatus(Long foodId)throws Exception;
}
