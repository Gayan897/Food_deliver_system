package com.fooddelivery.food_delivery.controller;

import com.fooddelivery.food_delivery.entity.Notification;
import com.fooddelivery.food_delivery.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:3000")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Get all notifications for user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Long userId) {
        try {
            List<Notification> notifications = notificationService.getUserNotifications(userId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get unread notifications
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable Long userId) {
        try {
            List<Notification> notifications = notificationService.getUnreadNotifications(userId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get unread count
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<?> getUnreadCount(@PathVariable Long userId) {
        try {
            Long count = notificationService.getUnreadCount(userId);
            return ResponseEntity.ok(Map.of("count", count));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Create notification
    @PostMapping
    public ResponseEntity<?> createNotification(@RequestBody Notification notification) {
        try {
            System.out.println("📬 Received notification request:");
            System.out.println("   User ID: " + notification.getUserId());
            System.out.println("   Title: " + notification.getTitle());
            System.out.println("   Message: " + notification.getMessage());
            System.out.println("   Type: " + notification.getType());
            System.out.println("   Reference ID: " + notification.getReferenceId());
            System.out.println("   Is Read: " + notification.getIsRead());

            // Validate required fields
            if (notification.getUserId() == null) {
                System.err.println("❌ User ID is null");
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "User ID is required"));
            }

            if (notification.getTitle() == null || notification.getTitle().trim().isEmpty()) {
                System.err.println("❌ Title is null or empty");
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Title is required"));
            }

            if (notification.getType() == null) {
                System.err.println("❌ Type is null");
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Type is required"));
            }

            // Set defaults
            if (notification.getIsRead() == null) {
                notification.setIsRead(false);
            }

            Notification created = notificationService.createNotification(notification);
            System.out.println("✅ Notification created successfully with ID: " + created.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            System.err.println("❌ Error creating notification: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Mark as read
    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        try {
            Notification notification = notificationService.markAsRead(id);
            return ResponseEntity.ok(notification);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Mark all as read
    @PutMapping("/user/{userId}/read-all")
    public ResponseEntity<?> markAllAsRead(@PathVariable Long userId) {
        try {
            notificationService.markAllAsRead(userId);
            return ResponseEntity.ok(Map.of("message", "All notifications marked as read"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Delete notification
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        try {
            notificationService.deleteNotification(id);
            return ResponseEntity.ok(Map.of("message", "Notification deleted"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // Delete all notifications
    @DeleteMapping("/user/{userId}/all")
    public ResponseEntity<?> deleteAllNotifications(@PathVariable Long userId) {
        try {
            notificationService.deleteAllNotifications(userId);
            return ResponseEntity.ok(Map.of("message", "All notifications deleted"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}