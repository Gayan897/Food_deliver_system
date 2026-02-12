package com.fooddelivery.food_delivery.service;

import com.fooddelivery.food_delivery.model.Order;
import com.fooddelivery.food_delivery.model.User;
import com.fooddelivery.food_delivery.request.OrderRequest;

import java.util.List;

public interface OrderService {

    public Order createOrder(OrderRequest order, User user);

    public Order updateOrder(Long orderId, String orderStatus)throws Exception;

    public void cancelOrder(Long orderId)throws Exception;

    public List<Order> getUsersOrder(Long userId)throws Exception;

    public List<Order> getRestaurantsOrder(Long restaurantId,String orderStaus)throws Exception;
}
