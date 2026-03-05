package com.fooddelivery.food_delivery.repository;

import com.fooddelivery.food_delivery.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem,Long> {
    List<MenuItem> findByRestaurantId(Long restaurantId);
    List<MenuItem> findByRestaurantIdAndIsAvailableTrue(Long restaurantId);
    List<MenuItem> findByCategoryIgnoreCase(String category);
    List<MenuItem> findByRestaurantIdAndCategoryIgnoreCase(Long restaurantId, String category);
}
