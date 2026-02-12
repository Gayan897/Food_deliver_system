package com.fooddelivery.food_delivery.request;

import com.fooddelivery.food_delivery.model.Address;
import lombok.Data;

@Data
public class OrderRequest {

   private Long restaurantId;
   private Address deliveryAddress;



}
