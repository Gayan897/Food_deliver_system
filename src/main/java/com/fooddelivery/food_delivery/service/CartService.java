package com.fooddelivery.food_delivery.service;

import com.fooddelivery.food_delivery.dto.AddToCartRequest;
import com.fooddelivery.food_delivery.dto.CartResponse;
import com.fooddelivery.food_delivery.dto.UpdateCartItemRequest;
import com.fooddelivery.food_delivery.entity.Cart;
import com.fooddelivery.food_delivery.entity.CartItem;
import com.fooddelivery.food_delivery.repository.CartItemRepository;
import com.fooddelivery.food_delivery.repository.CartRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

        @Autowired
        private CartRepository cartRepository;

        @Autowired
        private CartItemRepository cartItemRepository;

        public CartResponse getCartByUserId(Long userId) {
            Cart cart = cartRepository.findByUserId(userId)
                    .orElseGet(() -> createNewCart(userId));
            return convertToResponse(cart);
        }

        @Transactional
        public CartResponse addToCart(AddToCartRequest request) {
            Cart cart = cartRepository.findByUserId(request.getUserId())
                    .orElseGet(() -> createNewCart(request.getUserId()));

            // Check if cart has items from different restaurant
            if (cart.getRestaurantId() != null &&
                    !cart.getRestaurantId().equals(request.getRestaurantId())) {
                throw new RuntimeException("Cannot add items from different restaurants. Please clear cart first.");
            }

            // Update cart restaurant
            cart.setRestaurantId(request.getRestaurantId());

            // Check if item already exists in cart
            Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndMenuItemId(
                    cart.getId(), request.getMenuItemId());

            if (existingItem.isPresent()) {
                // Update quantity
                CartItem item = existingItem.get();
                item.setQuantity(item.getQuantity() + request.getQuantity());
                cartItemRepository.save(item);
            } else {
                // Add new item
                CartItem newItem = new CartItem();
                newItem.setCart(cart);
                newItem.setMenuItemId(request.getMenuItemId());
                newItem.setMenuItemName(request.getMenuItemName());
                newItem.setQuantity(request.getQuantity());
                newItem.setPrice(request.getPrice());
                newItem.setCustomizations(request.getCustomizations());
                newItem.setRestaurantId(request.getRestaurantId());
                newItem.setRestaurantName(request.getRestaurantName());

                cart.getItems().add(newItem);
                cartItemRepository.save(newItem);
            }

            cart.calculateTotals();
            cartRepository.save(cart);

            return convertToResponse(cart);
        }

        @Transactional
        public CartResponse updateCartItem(Long cartItemId, UpdateCartItemRequest request) {
            CartItem cartItem = cartItemRepository.findById(cartItemId)
                    .orElseThrow(() -> new RuntimeException("Cart item not found"));

            cartItem.setQuantity(request.getQuantity());
            if (request.getCustomizations() != null) {
                cartItem.setCustomizations(request.getCustomizations());
            }

            cartItemRepository.save(cartItem);

            Cart cart = cartItem.getCart();
            cart.calculateTotals();
            cartRepository.save(cart);

            return convertToResponse(cart);
        }

        @Transactional
        public CartResponse removeCartItem(Long cartItemId) {
            CartItem cartItem = cartItemRepository.findById(cartItemId)
                    .orElseThrow(() -> new RuntimeException("Cart item not found"));

            Cart cart = cartItem.getCart();
            cart.getItems().remove(cartItem);
            cartItemRepository.delete(cartItem);

            // If cart is empty, reset restaurant
            if (cart.getItems().isEmpty()) {
                cart.setRestaurantId(null);
            }

            cart.calculateTotals();
            cartRepository.save(cart);

            return convertToResponse(cart);
        }

        @Transactional
        public void clearCart(Long userId) {
            Cart cart = cartRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Cart not found"));

            cart.getItems().clear();
            cart.setRestaurantId(null);
            cart.setTotalAmount(0.0);
            cart.setTotalItems(0);

            cartRepository.save(cart);
        }

        public Double getCartTotal(Long userId) {
            Cart cart = cartRepository.findByUserId(userId)
                    .orElseGet(() -> createNewCart(userId));
            return cart.getTotalAmount();
        }

        private Cart createNewCart(Long userId) {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setTotalAmount(0.0);
            cart.setTotalItems(0);
            return cartRepository.save(cart);
        }

        private CartResponse convertToResponse(Cart cart) {
            return new CartResponse(
                    cart.getId(),
                    cart.getUserId(),
                    cart.getRestaurantId(),
                    cart.getItems(),
                    cart.getTotalAmount(),
                    cart.getTotalItems()
            );
        }
}
