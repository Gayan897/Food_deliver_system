package com.fooddelivery.food_delivery.controller;

import com.fooddelivery.food_delivery.dto.AddressRequest;
import com.fooddelivery.food_delivery.dto.AddressResponse;
import com.fooddelivery.food_delivery.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @GetMapping("/{userId}/addresses")
    public ResponseEntity<List<AddressResponse>> getUserAddresses(@PathVariable Long userId) {
        List<AddressResponse> addresses = addressService.getUserAddresses(userId);
        return ResponseEntity.ok(addresses);
    }

    @PostMapping("/{userId}/addresses")
    public ResponseEntity<?> addAddress(@PathVariable Long userId,
                                        @Valid @RequestBody AddressRequest request) {
        try {
            AddressResponse response = addressService.addAddress(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/addresses/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable Long id,
                                           @Valid @RequestBody AddressRequest request) {
        try {
            AddressResponse response = addressService.updateAddress(id, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id) {
        try {
            addressService.deleteAddress(id);
            return ResponseEntity.ok("Address deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
