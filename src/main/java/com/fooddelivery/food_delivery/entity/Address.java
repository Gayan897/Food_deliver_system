package com.fooddelivery.food_delivery.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "address_line", nullable = false)
    private String addressLine;

    @Column(nullable = false)
    private String city;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    private String state;

    private String landmark;

    @Column(name = "is_default")
    private Boolean isDefault = false;

    @Enumerated(EnumType.STRING)
    private AddressType type = AddressType.HOME;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum AddressType {
        HOME, WORK, OTHER
    }
}
