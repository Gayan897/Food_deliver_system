package com.fooddelivery.food_delivery.service;

import com.fooddelivery.food_delivery.model.Cart;
import com.fooddelivery.food_delivery.model.CartItem;
import com.fooddelivery.food_delivery.model.Food;
import com.fooddelivery.food_delivery.model.User;
import com.fooddelivery.food_delivery.repository.CartItemRepository;
import com.fooddelivery.food_delivery.repository.CartRepository;
import com.fooddelivery.food_delivery.repository.FoodRepository;
import com.fooddelivery.food_delivery.request.AddCartItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImp implements CartService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private FoodService foodService;



    @Override
    public CartItem addItemToCart(AddCartItemRequest req, String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        Food food = foodService.findFoodById(req.getFoodId());

        Cart cart=CartRepository.findByCustomerId(user.getId());

        for(CartItem cartItem : cart.getItems()){
            if(cartItem.getFood().equals(food)){
                int newQuantity=cartItem.getQuantity()+req.getQuantity();
                return updateCartItemQuantity(cartItem.getId(),newQuantity);
            }
        }

        return null;
    }

    @Override
    public CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws Exception {
        return null;
    }

    @Override
    public Cart removeItemFromCart(Long cartItemId, String jwt) throws Exception {
        return null;
    }

    @Override
    public Long calculateCartTotals(Cart cart) throws Exception {
        return 0L;
    }

    @Override
    public Cart findCartById(Long id) throws Exception {
        return null;
    }

    @Override
    public Cart findCartByUserId(Long userId) throws Exception {
        return null;
    }

    @Override
    public Cart clearCart(Long userId) throws Exception {
        return null;
    }
}
