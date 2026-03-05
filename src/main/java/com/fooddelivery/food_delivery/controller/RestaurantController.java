package com.fooddelivery.food_delivery.controller;


import com.fooddelivery.food_delivery.entity.Restaurant;
import com.fooddelivery.food_delivery.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/restaurants")
@CrossOrigin(origins = "http://localhost:3000")
public class RestaurantController {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        try {
            List<Restaurant> restaurants = restaurantRepository.findAll();
            System.out.println("📊 Returning " + restaurants.size() + " restaurants");
            return ResponseEntity.ok(restaurants);
        } catch (Exception e) {
            System.err.println("❌ Error fetching restaurants: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRestaurantById(@PathVariable Long id) {
        try {
            Restaurant restaurant = restaurantRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Restaurant not found"));
            return ResponseEntity.ok(restaurant);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createRestaurant(@RequestBody Restaurant restaurant) {
        try {
            System.out.println("➕ Creating restaurant: " + restaurant.getName());
            System.out.println("   Rating: " + restaurant.getRating());
            System.out.println("   Total Ratings: " + restaurant.getTotalRatings());
            System.out.println("   Is Active: " + restaurant.getIsActive());

            // ✅ Ensure defaults are set
            if (restaurant.getRating() == null) {
                restaurant.setRating(0.0);
            }
            if (restaurant.getTotalRatings() == null) {
                restaurant.setTotalRatings(0);
            }
            if (restaurant.getIsActive() == null) {
                restaurant.setIsActive(true);
            }

            Restaurant savedRestaurant = restaurantRepository.save(restaurant);
            System.out.println("✅ Restaurant created with ID: " + savedRestaurant.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(savedRestaurant);
        } catch (Exception e) {
            System.err.println("❌ Error creating restaurant: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRestaurant(@PathVariable Long id, @RequestBody Restaurant restaurantDetails) {
        try {
            System.out.println("🔄 Updating restaurant ID: " + id);
            System.out.println("   New Rating: " + restaurantDetails.getRating());
            System.out.println("   New Total Ratings: " + restaurantDetails.getTotalRatings());
            System.out.println("   New Status: " + restaurantDetails.getIsActive());

            Restaurant restaurant = restaurantRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Restaurant not found"));

            // ✅ Update all fields
            restaurant.setName(restaurantDetails.getName());
            restaurant.setCuisine(restaurantDetails.getCuisine());
            restaurant.setAddress(restaurantDetails.getAddress());
            restaurant.setPhone(restaurantDetails.getPhone());
            restaurant.setOpeningTime(restaurantDetails.getOpeningTime());
            restaurant.setClosingTime(restaurantDetails.getClosingTime());
            restaurant.setImageUrl(restaurantDetails.getImageUrl());
            restaurant.setDescription(restaurantDetails.getDescription());
            restaurant.setDeliveryTime(restaurantDetails.getDeliveryTime());
            restaurant.setMinOrderAmount(restaurantDetails.getMinOrderAmount());

            // ✅ CRITICAL: Update rating and status
            restaurant.setRating(restaurantDetails.getRating());
            restaurant.setTotalRatings(restaurantDetails.getTotalRatings());
            restaurant.setIsActive(restaurantDetails.getIsActive());

            Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
            System.out.println("✅ Restaurant updated successfully");

            return ResponseEntity.ok(updatedRestaurant);
        } catch (Exception e) {
            System.err.println("❌ Error updating restaurant: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRestaurant(@PathVariable Long id) {
        try {
            System.out.println("🗑️ Deleting restaurant ID: " + id);

            Restaurant restaurant = restaurantRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Restaurant not found"));

            restaurantRepository.delete(restaurant);
            System.out.println("✅ Restaurant deleted successfully");

            return ResponseEntity.ok(Map.of("message", "Restaurant deleted successfully"));
        } catch (Exception e) {
            System.err.println("❌ Error deleting restaurant: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Restaurant>> searchRestaurants(
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) String location) {
        try {
            List<Restaurant> restaurants = restaurantRepository.findAll();

            // Filter by cuisine
            if (cuisine != null && !cuisine.isEmpty()) {
                restaurants = restaurants.stream()
                        .filter(r -> r.getCuisine().equalsIgnoreCase(cuisine))
                        .toList();
            }

            // Filter by location (address contains)
            if (location != null && !location.isEmpty()) {
                restaurants = restaurants.stream()
                        .filter(r -> r.getAddress().toLowerCase().contains(location.toLowerCase()))
                        .toList();
            }

            return ResponseEntity.ok(restaurants);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}