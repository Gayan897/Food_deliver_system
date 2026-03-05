package com.fooddelivery.food_delivery.service;

import com.fooddelivery.food_delivery.dto.MenuItemRequest;
import com.fooddelivery.food_delivery.entity.MenuItem;
import com.fooddelivery.food_delivery.entity.Restaurant;
import com.fooddelivery.food_delivery.repository.MenuItemRepository;
import com.fooddelivery.food_delivery.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemService {
    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    public List<MenuItem> getRestaurantMenu(Long restaurantId) {
        return menuItemRepository.findByRestaurantIdAndIsAvailableTrue(restaurantId);
    }

    public List<MenuItem> getMenuItemsByCategory(String category) {
        return menuItemRepository.findByCategoryIgnoreCase(category);
    }

    public MenuItem getMenuItemById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));
    }

    @Transactional
    public MenuItem createMenuItem(Long restaurantId, MenuItemRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        MenuItem menuItem = new MenuItem();
        menuItem.setRestaurant(restaurant);
        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setCategory(request.getCategory());
        menuItem.setIsVeg(request.getIsVeg());
        menuItem.setIsAvailable(request.getIsAvailable());
        menuItem.setImageUrl(request.getImageUrl());
        menuItem.setPreparationTime(request.getPreparationTime());
        menuItem.setIsBestseller(request.getIsBestseller());
        menuItem.setDiscountPercentage(request.getDiscountPercentage());

        return menuItemRepository.save(menuItem);
    }

    @Transactional
    public MenuItem updateMenuItem(Long id, MenuItemRequest request) {
        MenuItem menuItem = getMenuItemById(id);

        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setCategory(request.getCategory());
        menuItem.setIsVeg(request.getIsVeg());
        menuItem.setIsAvailable(request.getIsAvailable());
        menuItem.setImageUrl(request.getImageUrl());
        menuItem.setPreparationTime(request.getPreparationTime());
        menuItem.setIsBestseller(request.getIsBestseller());
        menuItem.setDiscountPercentage(request.getDiscountPercentage());

        return menuItemRepository.save(menuItem);
    }

    @Transactional
    public void deleteMenuItem(Long id) {
        menuItemRepository.deleteById(id);
    }
}
