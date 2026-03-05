package com.fooddelivery.food_delivery.controller;

import com.fooddelivery.food_delivery.entity.MenuItem;
import com.fooddelivery.food_delivery.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/menu")
@CrossOrigin(origins = "http://localhost:3000")
public class MenuItemController {

    @Autowired
    private MenuItemRepository menuItemRepository;

    // ✅ GET MENU BY RESTAURANT ID
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItem>> getMenuByRestaurant(@PathVariable Long restaurantId) {
        try {
            System.out.println("🍽️ Fetching menu for restaurant: " + restaurantId);

            List<MenuItem> menuItems = menuItemRepository.findByRestaurantIdAndIsAvailableTrue(restaurantId);

            System.out.println("✅ Found " + menuItems.size() + " menu items");
            return ResponseEntity.ok(menuItems);
        } catch (Exception e) {
            System.err.println("❌ Error fetching menu: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // ✅ GET ALL MENU ITEMS
    @GetMapping
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        try {
            List<MenuItem> menuItems = menuItemRepository.findAll();
            return ResponseEntity.ok(menuItems);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ✅ GET MENU ITEM BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getMenuItemById(@PathVariable Long id) {
        try {
            MenuItem menuItem = menuItemRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Menu item not found"));
            return ResponseEntity.ok(menuItem);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ CREATE MENU ITEM
    @PostMapping
    public ResponseEntity<?> createMenuItem(@RequestBody MenuItem menuItem) {
        try {
            System.out.println("➕ Creating menu item: " + menuItem.getName());

            MenuItem savedMenuItem = menuItemRepository.save(menuItem);

            System.out.println("✅ Menu item created with ID: " + savedMenuItem.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMenuItem);
        } catch (Exception e) {
            System.err.println("❌ Error creating menu item: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ UPDATE MENU ITEM
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMenuItem(@PathVariable Long id, @RequestBody MenuItem menuItemDetails) {
        try {
            System.out.println("🔄 Updating menu item: " + id);

            MenuItem menuItem = menuItemRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Menu item not found"));

            menuItem.setName(menuItemDetails.getName());
            menuItem.setDescription(menuItemDetails.getDescription());
            menuItem.setPrice(menuItemDetails.getPrice());
            menuItem.setImageUrl(menuItemDetails.getImageUrl());
            menuItem.setCategory(menuItemDetails.getCategory());
            menuItem.setIsVeg(menuItemDetails.getIsVeg());
            menuItem.setIsAvailable(menuItemDetails.getIsAvailable());
            menuItem.setPreparationTime(menuItemDetails.getPreparationTime());

            MenuItem updatedMenuItem = menuItemRepository.save(menuItem);

            System.out.println("✅ Menu item updated successfully");
            return ResponseEntity.ok(updatedMenuItem);
        } catch (Exception e) {
            System.err.println("❌ Error updating menu item: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ DELETE MENU ITEM
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMenuItem(@PathVariable Long id) {
        try {
            System.out.println("🗑️ Deleting menu item: " + id);

            MenuItem menuItem = menuItemRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Menu item not found"));

            menuItemRepository.delete(menuItem);

            System.out.println("✅ Menu item deleted successfully");
            return ResponseEntity.ok(Map.of("message", "Menu item deleted successfully"));
        } catch (Exception e) {
            System.err.println("❌ Error deleting menu item: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}