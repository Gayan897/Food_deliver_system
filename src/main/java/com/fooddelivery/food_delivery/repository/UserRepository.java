package com.fooddelivery.food_delivery.repository;

import com.fooddelivery.food_delivery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    public User findByEmail(String username);
}
