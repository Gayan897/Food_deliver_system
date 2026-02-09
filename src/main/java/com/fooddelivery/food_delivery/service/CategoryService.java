package com.fooddelivery.food_delivery.service;

import com.fooddelivery.food_delivery.model.Category;

import java.util.List;

public interface CategoryService {

    public Category createCategory(String name,Long, userId);

    public List<Category> findCategoryByRestaurantId(Long id)throws Exception;

    public Category findCategoryById(Long id)throws Exception;
}
