package com.fooddelivery.food_delivery.controller;

import com.fooddelivery.food_delivery.dto.PaymentRequest;
import com.fooddelivery.food_delivery.entity.Payment;
import com.fooddelivery.food_delivery.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {
    @Autowired
    private PaymentService service;

    @PostMapping("/process")
    public ResponseEntity<?> processPayment(@Valid @RequestBody PaymentRequest request) {
        try {
            Payment payment = service.processPayment(request);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getByOrderId(@PathVariable Long orderId) {
        try {
            Payment payment = service.getByOrderId(orderId);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<?> refund(@PathVariable Long id) {
        try {
            Payment payment = service.refundPayment(id);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
