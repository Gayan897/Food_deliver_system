package com.fooddelivery.food_delivery.request;

import com.fooddelivery.food_delivery.model.Address;
import com.fooddelivery.food_delivery.model.ContactInformation;

import java.util.List;

public class CreateRestaurantRequest {
    private Long id;
    private String name;
    private String description;
    private Address address;
    private ContactInformation contactInformation;
    private String openingHours;
    private List<String> images;
}
