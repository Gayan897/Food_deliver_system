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
@CrossOrigin(origins = "http://localhost:3000")
public class DeliveryController {

    @Autowired
    private DeliveryService service;

    // ✅ ADD THIS: GET ALL DELIVERY PERSONS
    @GetMapping
    public ResponseEntity<List<DeliveryPersonResponse>> getAllDeliveryPersons() {
        try {
            System.out.println("🚴 Fetching all delivery persons...");
            List<DeliveryPersonResponse> persons = service.getAllDeliveryPersons();
            System.out.println("✅ Found " + persons.size() + " delivery persons");
            return ResponseEntity.ok(persons);
        } catch (Exception e) {
            System.err.println("❌ Error fetching delivery persons: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody DeliveryPersonRequest request) {
        try {
            DeliveryPersonResponse response = service.registerDeliveryPerson(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            DeliveryPersonResponse response = service.getById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
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
            System.out.println("🔄 Updating availability for delivery person " + id + " to: " + isAvailable);

            DeliveryPersonResponse response = service.updateAvailability(id, isAvailable);

            System.out.println("✅ Availability updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ Error updating availability: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDeliveryPerson(
            @PathVariable Long id,
            @Valid @RequestBody DeliveryPersonRequest request) {
        try {
            System.out.println("🔄 Updating delivery person: " + id);

            DeliveryPersonResponse response = service.updateDeliveryPerson(id, request);

            System.out.println("✅ Delivery person updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ Error updating delivery person: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ ADD THIS: DELETE DELIVERY PERSON
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDeliveryPerson(@PathVariable Long id) {
        try {
            System.out.println("🗑️ Deleting delivery person: " + id);
            service.deleteDeliveryPerson(id);
            System.out.println("✅ Delivery person deleted successfully");
            return ResponseEntity.ok(Map.of("message", "Delivery person deleted successfully"));
        } catch (Exception e) {
            System.err.println("❌ Error deleting: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}