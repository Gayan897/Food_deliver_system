package com.fooddelivery.food_delivery.controller;

import com.fooddelivery.food_delivery.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/stats")
    public ResponseEntity<?> getAdminStats() {
        try {
            Map<String, Object> stats = adminService.getAdminStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/recent-orders")
    public ResponseEntity<?> getRecentOrders(@RequestParam(defaultValue = "5") int limit) {
        try {
            return ResponseEntity.ok(adminService.getRecentOrders(limit));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
