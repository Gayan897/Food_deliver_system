package com.fooddelivery.food_delivery.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @Column(name = "reference_id")
    private Long referenceId; // Order ID, Restaurant ID, etc.

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum NotificationType {
        ORDER,           // Order related
        RESTAURANT,      // Restaurant related
        USER,           // User related
        DELIVERY,       // Delivery related
        SYSTEM,         // System announcements
        PAYMENT,        // Payment related
        REVIEW          // Review related
    }
}