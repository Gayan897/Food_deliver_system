package com.fooddelivery.food_delivery.dto;

import com.fooddelivery.food_delivery.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private Long id;
    private Long userId;
    private Long restaurantId;
    private List<CartItem> items;
    private Double totalAmount;
    private Integer totalItems;
}
