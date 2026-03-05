package com.fooddelivery.food_delivery.service;

import com.fooddelivery.food_delivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Map<String, Object> getAdminStats() {
        Map<String, Object> stats = new HashMap<>();

        try {
            // Get total users
            long totalUsers = userRepository.count();
            stats.put("totalUsers", totalUsers);

            // Get total restaurants from restaurant service
            try {
                String restaurantUrl = "http://localhost:8080/api/restaurants";
                Object[] restaurants = restTemplate.getForObject(restaurantUrl, Object[].class);
                stats.put("totalRestaurants", restaurants != null ? restaurants.length : 0);
            } catch (Exception e) {
                System.err.println("Could not fetch restaurants: " + e.getMessage());
                stats.put("totalRestaurants", 0);
            }

            // Get order statistics from order service
            try {
                String orderStatsUrl = "http://localhost:8080/api/orders/stats";
                Map<String, Object> orderStats = restTemplate.getForObject(orderStatsUrl, Map.class);
                if (orderStats != null) {
                    stats.put("totalOrders", orderStats.getOrDefault("totalOrders", 0));
                    stats.put("activeOrders", orderStats.getOrDefault("activeOrders", 0));
                    stats.put("pendingOrders", orderStats.getOrDefault("pendingOrders", 0));
                    stats.put("totalRevenue", orderStats.getOrDefault("totalRevenue", 0.0));
                }
            } catch (Exception e) {
                System.err.println("Could not fetch order stats: " + e.getMessage());
                stats.put("totalOrders", 0);
                stats.put("activeOrders", 0);
                stats.put("pendingOrders", 0);
                stats.put("totalRevenue", 0.0);
            }

        } catch (Exception e) {
            System.err.println("Error getting admin stats: " + e.getMessage());
            throw new RuntimeException("Failed to get admin stats");
        }

        return stats;
    }

    public List<Map<String, Object>> getRecentOrders(int limit) {
        try {
            String orderUrl = "http://localhost:8080/api/orders/recent?limit=" + limit;
            Object[] orders = restTemplate.getForObject(orderUrl, Object[].class);
            return orders != null ? Arrays.asList((Map<String, Object>[]) orders) : new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Could not fetch recent orders: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
