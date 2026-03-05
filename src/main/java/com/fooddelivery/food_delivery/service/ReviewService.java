package com.fooddelivery.food_delivery.service;


import com.fooddelivery.food_delivery.dto.ReviewRequest;
import com.fooddelivery.food_delivery.entity.Review;
import com.fooddelivery.food_delivery.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository repository;

    @Transactional
    public Review createReview(ReviewRequest request) {
        Review review = new Review();
        review.setUserId(request.getUserId());
        review.setTargetType(Review.TargetType.valueOf(request.getTargetType().toUpperCase()));
        review.setTargetId(request.getTargetId());
        review.setOrderId(request.getOrderId());
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        return repository.save(review);
    }

    public List<Review> getReviewsFor(String targetType, Long targetId) {
        Review.TargetType type = Review.TargetType.valueOf(targetType.toUpperCase());
        return repository.findByTargetTypeAndTargetId(type, targetId);
    }

    public List<Review> getUserReviews(Long userId) {
        return repository.findByUserId(userId);
    }
}
