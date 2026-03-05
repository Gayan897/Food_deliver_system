package com.fooddelivery.food_delivery.service;

import com.fooddelivery.food_delivery.entity.Notification;
import com.fooddelivery.food_delivery.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // Get all notifications for a user
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // Get unread notifications for a user
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
    }

    // Get unread count
    public Long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    // Create notification
    @Transactional
    public Notification createNotification(Notification notification) {
        System.out.println("📬 Creating notification for user: " + notification.getUserId());
        return notificationRepository.save(notification);
    }

    // Mark as read
    @Transactional
    public Notification markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setIsRead(true);
        return notificationRepository.save(notification);
    }

    // Mark all as read
    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> notifications = notificationRepository
                .findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
        notifications.forEach(n -> n.setIsRead(true));
        notificationRepository.saveAll(notifications);
    }

    // Delete notification
    @Transactional
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    // Delete all for user
    @Transactional
    public void deleteAllNotifications(Long userId) {
        List<Notification> notifications = notificationRepository
                .findByUserIdOrderByCreatedAtDesc(userId);
        notificationRepository.deleteAll(notifications);
    }
}