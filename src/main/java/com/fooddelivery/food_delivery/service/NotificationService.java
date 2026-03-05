package com.fooddelivery.food_delivery.service;

import com.fooddelivery.food_delivery.dto.SendNotificationRequest;
import com.fooddelivery.food_delivery.entity.Notification;
import com.fooddelivery.food_delivery.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository repository;

    @Transactional
    public Notification sendNotification(SendNotificationRequest request) {
        Notification notification = new Notification();
        notification.setUserId(request.getUserId());
        notification.setType(Notification.NotificationType.valueOf(request.getType().toUpperCase()));
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setOrderId(request.getOrderId());
        notification.setIsRead(false);

        // Here you would integrate with actual email/SMS/push services
        System.out.println("📧 Sending notification to user: " + request.getUserId());

        return repository.save(notification);
    }

    public List<Notification> getUserNotifications(Long userId) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional
    public Notification markAsRead(Long id) {
        Notification notification = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setIsRead(true);
        return repository.save(notification);
    }

}
