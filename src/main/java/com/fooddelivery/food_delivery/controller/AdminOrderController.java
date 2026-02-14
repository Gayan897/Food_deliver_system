package com.fooddelivery.food_delivery.controller;

import com.fooddelivery.food_delivery.model.Order;
import com.fooddelivery.food_delivery.model.User;
import com.fooddelivery.food_delivery.service.OrderService;
import com.fooddelivery.food_delivery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminOrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PutMapping("/order/{id}/{orderStatus}")
    private ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam(required = false)String orderStatus,
            @RequestHeader("Authorization") String jwt)throws Exception{
        User user=userService.findUserByJwtToken(jwt);
        Order orders = orderService.updateOrder(id,orderStatus);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    @GetMapping("/order/user/restaurant/{id}")
    private ResponseEntity<List<Order>> getOrderHistory(
            @PathVariable Long id,
            @RequestParam(required = false)String order_status,
            @RequestHeader("Authorization") String jwt)throws Exception{
        User user=userService.findUserByJwtToken(jwt);
        List<Order> orders = orderService.getRestaurantsOrder(id,order_status);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

}
