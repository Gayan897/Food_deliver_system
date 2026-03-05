package com.fooddelivery.food_delivery.repository;

import com.fooddelivery.food_delivery.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long>
{
    List<Category> findByRestaurantIdOrderByDisplayOrderAsc(Long restaurantId);
}
