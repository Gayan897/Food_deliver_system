package com.fooddelivery.food_delivery.controller;

import com.fooddelivery.food_delivery.model.Cart;
import com.fooddelivery.food_delivery.model.CartItem;
import com.fooddelivery.food_delivery.model.User;
import com.fooddelivery.food_delivery.request.AddCartItemRequest;
import com.fooddelivery.food_delivery.request.UpdateCartItemRequest;
import com.fooddelivery.food_delivery.service.CartService;
import com.fooddelivery.food_delivery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @PutMapping("/cart/add")
    private ResponseEntity<CartItem> addItemToCart(@RequestBody AddCartItemRequest req, @RequestHeader("Authorization") String jwt)throws Exception{
        CartItem cartItem=cartService.addItemToCart(req,jwt);
        return new ResponseEntity<>(cartItem, HttpStatus.OK);
    }
    @PutMapping("/cart-item/update")
    private ResponseEntity<CartItem> updatetoCartItemQuantity(@RequestBody UpdateCartItemRequest req, @RequestHeader("Authorization") String jwt)throws Exception{
        CartItem cartItem=cartService.updateCartItemQuantity(req.getCartItemId(),req.getQuantity());
        return new ResponseEntity<>(cartItem, HttpStatus.OK);
    }
    @PutMapping("/cart-item/{id}/remove")
    private ResponseEntity<Cart> RemoveCartItem(@PathVariable Long id, @RequestHeader("Authorization") String jwt)throws Exception{
        Cart cart=cartService.removeItemFromCart(id,jwt);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
    @PutMapping("/cart/clear")
    private ResponseEntity<Cart> ClearCart (
            @RequestHeader("Authorization") String jwt)throws Exception{
        User user=userService.findUserByJwtToken(jwt);
        Cart cart=cartService.clearCart(Long.valueOf(String.valueOf(user.getId())));
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
    @GetMapping("/cart")
    private ResponseEntity<Cart> findUserCart ( @RequestHeader("Authorization") String jwt)throws Exception{
        User user=userService.findUserByJwtToken(jwt);
        Cart cart=cartService.findCartByUserId(user.getId());
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }


}
