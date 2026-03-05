package com.fooddelivery.food_delivery.repository;

import com.fooddelivery.food_delivery.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long>
{
    List<Address> findByUserId(Long userId);
}
