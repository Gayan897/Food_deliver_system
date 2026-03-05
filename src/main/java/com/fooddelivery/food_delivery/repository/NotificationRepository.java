package com.fooddelivery.food_delivery.repository;

import com.fooddelivery.food_delivery.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Notification> findByUserIdAndIsReadOrderByCreatedAtDesc(Long userId, Boolean isRead);
    Long countByUserIdAndIsReadFalse(Long userId);
    List<Notification> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);
}
