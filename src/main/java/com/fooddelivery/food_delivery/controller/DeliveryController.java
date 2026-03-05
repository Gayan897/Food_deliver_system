package com.fooddelivery.food_delivery.controller;

import com.fooddelivery.food_delivery.dto.DeliveryPersonRequest;
import com.fooddelivery.food_delivery.dto.DeliveryPersonResponse;
import com.fooddelivery.food_delivery.service.DeliveryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/delivery")
@CrossOrigin(origins = "*")
public class DeliveryController {
    @Autowired
    private DeliveryService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody DeliveryPersonRequest request) {
        try {
            DeliveryPersonResponse response = service.registerDeliveryPerson(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            DeliveryPersonResponse response = service.getById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/available")
    public ResponseEntity<List<DeliveryPersonResponse>> getAvailable() {
        return ResponseEntity.ok(service.getAvailableDeliveryPersons());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> request) {
        try {
            Boolean isAvailable = request.get("isAvailable");
            DeliveryPersonResponse response = service.updateAvailability(id, isAvailable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/location")
    public ResponseEntity<?> updateLocation(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        try {
            Double lat = request.get("latitude");
            Double lng = request.get("longitude");
            DeliveryPersonResponse response = service.updateLocation(id, lat, lng);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
