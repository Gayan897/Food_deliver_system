package com.fooddelivery.food_delivery.repository;

import com.fooddelivery.food_delivery.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>
{
    List<Review> findByTargetTypeAndTargetId(Review.TargetType targetType, Long targetId);
    List<Review> findByUserId(Long userId);
}
