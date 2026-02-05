package com.fooddelivery.food_delivery.repository;

import com.fooddelivery.food_delivery.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {
}
