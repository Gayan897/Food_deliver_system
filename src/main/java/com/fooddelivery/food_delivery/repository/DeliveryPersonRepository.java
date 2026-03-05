package com.fooddelivery.food_delivery.repository;

import com.fooddelivery.food_delivery.entity.DeliveryPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryPersonRepository extends JpaRepository<DeliveryPerson, Long> {
    Optional<DeliveryPerson> findByEmail(String email);
    Optional<DeliveryPerson> findByPhone(String phone);
    List<DeliveryPerson> findByIsAvailableAndIsActive(Boolean isAvailable, Boolean isActive);
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);
}
