package com.fooddelivery.food_delivery.repository;

import com.fooddelivery.food_delivery.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
}
