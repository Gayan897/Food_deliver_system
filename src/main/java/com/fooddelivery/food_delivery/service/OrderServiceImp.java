package com.fooddelivery.food_delivery.service;

import com.fooddelivery.food_delivery.model.Address;
import com.fooddelivery.food_delivery.model.Order;
import com.fooddelivery.food_delivery.model.User;
import com.fooddelivery.food_delivery.repository.*;
import com.fooddelivery.food_delivery.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImp implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantService restaurantService;

    @Override
    public Order createOrder(OrderRequest order, User user) {

        Address shippingAddress=order.getDeliveryAddress();
        Address savedAddress=addressRepository.save(shippingAddress);

        if(!user.getAddresses().contains(savedAddress)){
            user.getAddresses().add(savedAddress);
            userRepository.save(user);
        }
        return null;
    }

    @Override
    public Order updateOrder(Long orderId, String orderStatus) throws Exception {
        return null;
    }

    @Override
    public void cancelOrder(Long orderId) throws Exception {

    }

    @Override
    public List<Order> getUsersOrder(Long userId) throws Exception {
        return List.of();
    }

    @Override
    public List<Order> getRestaurantsOrder(Long restaurantId, String orderStaus) throws Exception {
        return List.of();
    }
}
