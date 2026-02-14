package com.fooddelivery.food_delivery.controller;

import com.fooddelivery.food_delivery.model.CartItem;
import com.fooddelivery.food_delivery.model.Order;
import com.fooddelivery.food_delivery.model.User;
import com.fooddelivery.food_delivery.request.AddCartItemRequest;
import com.fooddelivery.food_delivery.request.OrderRequest;
import com.fooddelivery.food_delivery.service.OrderService;
import com.fooddelivery.food_delivery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("/order")
    private ResponseEntity<Order> createOrder(@RequestBody OrderRequest req, @RequestHeader("Authorization") String jwt)throws Exception{
        User user=userService.findUserByJwtToken(jwt);
        Order order = orderService.createOrder(req,user);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
    @GetMapping("/order/user")
    private ResponseEntity<List<Order>> getOrderHistory(@RequestHeader("Authorization") String jwt)throws Exception{
        User user=userService.findUserByJwtToken(jwt);
         List<Order> orders = orderService.getUsersOrder(user.getId());
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
