package com.fooddelivery.food_delivery.repository;

import com.fooddelivery.food_delivery.model.IngredientsItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientItemRepository extends JpaRepository <IngredientsItem,Long> {

    List<IngredientsItem> findByRestaurantId(Long id);
}
