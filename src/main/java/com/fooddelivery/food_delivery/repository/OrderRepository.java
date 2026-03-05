package com.fooddelivery.food_delivery.repository;

import com.fooddelivery.food_delivery.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends  JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByRestaurantId(Long restaurantId);
    List<Order> findByDeliveryPersonId(Long deliveryPersonId);
    List<Order> findByStatus(Order.OrderStatus status);
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
}
