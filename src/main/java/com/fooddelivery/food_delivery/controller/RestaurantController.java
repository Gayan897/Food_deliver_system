package com.fooddelivery.food_delivery.controller;

import com.fooddelivery.food_delivery.dto.RestaurantRequest;
import com.fooddelivery.food_delivery.entity.Restaurant;
import com.fooddelivery.food_delivery.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@CrossOrigin(origins = "*")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRestaurantById(@PathVariable Long id) {
        try {
            Restaurant restaurant = restaurantService.getRestaurantById(id);
            return ResponseEntity.ok(restaurant);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createRestaurant(@Valid @RequestBody RestaurantRequest request) {
        try {
            Restaurant restaurant = restaurantService.createRestaurant(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(restaurant);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRestaurant(@PathVariable Long id,
                                              @Valid @RequestBody RestaurantRequest request) {
        try {
            Restaurant restaurant = restaurantService.updateRestaurant(id, request);
            return ResponseEntity.ok(restaurant);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Restaurant>> searchRestaurants(
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) String location) {
        return ResponseEntity.ok(restaurantService.searchRestaurants(cuisine, location));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRestaurant(@PathVariable Long id) {
        try {
            restaurantService.deleteRestaurant(id);
            return ResponseEntity.ok("Restaurant deactivated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
