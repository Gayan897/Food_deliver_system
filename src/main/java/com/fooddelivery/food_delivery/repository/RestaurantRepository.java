package com.fooddelivery.food_delivery.repository;

import com.fooddelivery.food_delivery.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
    List<Restaurant> findByIsActiveTrue();
    List<Restaurant> findByCuisineContainingIgnoreCase(String cuisine);
    List<Restaurant> findByAddressContainingIgnoreCase(String location);
    List<Restaurant> findByNameContainingIgnoreCase(String name);
}
