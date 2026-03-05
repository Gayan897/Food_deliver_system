package com.fooddelivery.food_delivery.controller;

import com.fooddelivery.food_delivery.dto.ReviewRequest;
import com.fooddelivery.food_delivery.entity.Review;
import com.fooddelivery.food_delivery.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
// NOTE: @CrossOrigin here is redundant since SecurityConfig handles CORS globally,
// but keeping it doesn't hurt. Remove if you want to rely solely on SecurityConfig.
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"}, allowCredentials = "true")
public class ReviewController {

    @Autowired
    private ReviewService service;

    @PostMapping
    public ResponseEntity<?> createReview(@Valid @RequestBody ReviewRequest request) {
        // DEBUG - remove after fixing
        System.out.println("=== REVIEW CONTROLLER HIT ===");
        System.out.println("userId: " + request.getUserId());
        System.out.println("targetId: " + request.getTargetId());
        System.out.println("targetType: " + request.getTargetType());
        System.out.println("rating: " + request.getRating());

        try {
            Review review = service.createReview(request);
            return ResponseEntity.ok(review);
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage()); // DEBUG
            return ResponseEntity.badRequest().body("Invalid data: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage()); // DEBUG
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Server error: " + e.getMessage());
        }
    }

    @GetMapping("/{targetType}/{targetId}")
    public ResponseEntity<?> getReviews(
            @PathVariable String targetType,
            @PathVariable Long targetId) {
        try {
            List<Review> reviews = service.getReviewsFor(targetType, targetId);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getUserReviews(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getUserReviews(userId));
    }
}