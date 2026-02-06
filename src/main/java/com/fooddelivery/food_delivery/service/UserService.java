package com.fooddelivery.food_delivery.service;

import com.fooddelivery.food_delivery.model.User;

public interface UserService {

    public User findByJwtToken(String jwt) throws Exception;

    public User findUserByEmail(String email) throws  Exception;
}
