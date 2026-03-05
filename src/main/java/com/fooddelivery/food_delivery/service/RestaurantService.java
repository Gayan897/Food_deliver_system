package com.fooddelivery.food_delivery.service;

import com.fooddelivery.food_delivery.dto.RestaurantRequest;
import com.fooddelivery.food_delivery.entity.Restaurant;
import com.fooddelivery.food_delivery.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findByIsActiveTrue();
    }

    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }

    @Transactional
    public Restaurant createRestaurant(RestaurantRequest request) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setCuisine(request.getCuisine());
        restaurant.setAddress(request.getAddress());
        restaurant.setPhone(request.getPhone());
        restaurant.setOpeningTime(request.getOpeningTime());
        restaurant.setClosingTime(request.getClosingTime());
        restaurant.setImageUrl(request.getImageUrl());
        restaurant.setDescription(request.getDescription());
        restaurant.setDeliveryTime(request.getDeliveryTime());
        restaurant.setMinOrderAmount(request.getMinOrderAmount());
        restaurant.setIsActive(true);

        return restaurantRepository.save(restaurant);
    }

    @Transactional
    public Restaurant updateRestaurant(Long id, RestaurantRequest request) {
        Restaurant restaurant = getRestaurantById(id);

        restaurant.setName(request.getName());
        restaurant.setCuisine(request.getCuisine());
        restaurant.setAddress(request.getAddress());
        restaurant.setPhone(request.getPhone());
        restaurant.setOpeningTime(request.getOpeningTime());
        restaurant.setClosingTime(request.getClosingTime());
        restaurant.setImageUrl(request.getImageUrl());
        restaurant.setDescription(request.getDescription());
        restaurant.setDeliveryTime(request.getDeliveryTime());
        restaurant.setMinOrderAmount(request.getMinOrderAmount());

        return restaurantRepository.save(restaurant);
    }

    public List<Restaurant> searchRestaurants(String cuisine, String location) {
        if (cuisine != null && !cuisine.isEmpty()) {
            return restaurantRepository.findByCuisineContainingIgnoreCase(cuisine);
        } else if (location != null && !location.isEmpty()) {
            return restaurantRepository.findByAddressContainingIgnoreCase(location);
        }
        return getAllRestaurants();
    }

    @Transactional
    public void deleteRestaurant(Long id) {
        Restaurant restaurant = getRestaurantById(id);
        restaurant.setIsActive(false);
        restaurantRepository.save(restaurant);
    }
}
