package com.fooddelivery.food_delivery.service;

import com.fooddelivery.food_delivery.model.IngredientCategory;
import com.fooddelivery.food_delivery.model.IngredientsItem;

import java.util.List;

public interface IngredientService {

    public IngredientCategory createIngredientCategory(String name,Long restaurantId) throws Exception;

    public IngredientCategory findIngredientCategoryById(Long id) throws Exception;

    public List<IngredientCategory> findIngredientCategoryByRestaurantId(Long id) throws Exception;

    public IngredientsItem createIngredientItem(Long restaurantId, String ingredientName, Long categoryId) throws Exception;

    public List<IngredientsItem> findRestaurantsIngredients(Long retaurantId);

    public IngredientsItem updateStock(Long id) throws Exception;

}
