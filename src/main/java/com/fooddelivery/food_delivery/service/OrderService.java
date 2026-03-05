package com.fooddelivery.food_delivery.service;

import com.fooddelivery.food_delivery.dto.CreateOrderRequest;
import com.fooddelivery.food_delivery.dto.OrderItemRequest;
import com.fooddelivery.food_delivery.dto.OrderItemResponse;
import com.fooddelivery.food_delivery.dto.OrderResponse;
import com.fooddelivery.food_delivery.entity.Order;
import com.fooddelivery.food_delivery.entity.OrderItem;
import com.fooddelivery.food_delivery.repository.OrderRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        System.out.println("📝 Creating new order for user: " + request.getUserId());

        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setRestaurantId(request.getRestaurantId());
        order.setRestaurantName(request.getRestaurantName());
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setSubtotal(request.getSubtotal());
        order.setDeliveryFee(request.getDeliveryFee() != null ? request.getDeliveryFee() : BigDecimal.ZERO);
        order.setTax(request.getTax() != null ? request.getTax() : BigDecimal.ZERO);
        order.setTotal(request.getTotal());
        order.setPaymentMethod(Order.PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase()));
        order.setSpecialInstructions(request.getSpecialInstructions());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setPaymentStatus(Order.PaymentStatus.PENDING);

        // Calculate estimated delivery time (40 minutes from now)
        order.setEstimatedDeliveryTime(LocalDateTime.now().plusMinutes(40));

        // Add order items
        for (OrderItemRequest itemRequest : request.getItems()) {
            OrderItem item = new OrderItem();
            item.setMenuItemId(itemRequest.getMenuItemId());
            item.setMenuItemName(itemRequest.getMenuItemName());
            item.setQuantity(itemRequest.getQuantity());
            item.setPrice(itemRequest.getPrice());
            item.setSubtotal(itemRequest.getPrice().multiply(new BigDecimal(itemRequest.getQuantity())));
            item.setSpecialRequests(itemRequest.getSpecialRequests());
            order.addOrderItem(item);
        }

        Order savedOrder = orderRepository.save(order);
        System.out.println("✅ Order created successfully with ID: " + savedOrder.getId());

        return convertToResponse(savedOrder);
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));
        return convertToResponse(order);
    }

    public List<OrderResponse> getUserOrders(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getRestaurantOrders(Long restaurantId) {
        return orderRepository.findByRestaurantId(restaurantId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> getOrdersByStatus(String status) {
        Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status.toUpperCase());
        return orderRepository.findByStatus(orderStatus).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long id, String status) {
        System.out.println("🔄 Updating order " + id + " status to: " + status);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));

        Order.OrderStatus newStatus = Order.OrderStatus.valueOf(status.toUpperCase());
        order.setStatus(newStatus);

        // If delivered, set actual delivery time
        if (newStatus == Order.OrderStatus.DELIVERED) {
            order.setActualDeliveryTime(LocalDateTime.now());
        }

        Order updatedOrder = orderRepository.save(order);
        System.out.println("✅ Order status updated successfully");

        return convertToResponse(updatedOrder);
    }

    @Transactional
    public OrderResponse assignDeliveryPerson(Long orderId, Long deliveryPersonId) {
        System.out.println("🚴 Assigning delivery person " + deliveryPersonId + " to order " + orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        order.setDeliveryPersonId(deliveryPersonId);
        order.setStatus(Order.OrderStatus.OUT_FOR_DELIVERY);

        Order updatedOrder = orderRepository.save(order);
        System.out.println("✅ Delivery person assigned successfully");

        return convertToResponse(updatedOrder);
    }

    @Transactional
    public void cancelOrder(Long id, String reason) {
        System.out.println("❌ Cancelling order: " + id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));

        if (order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new RuntimeException("Cannot cancel delivered order");
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        order.setCancellationReason(reason);
        orderRepository.save(order);

        System.out.println("✅ Order cancelled successfully");
    }

    private OrderResponse convertToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setUserId(order.getUserId());
        response.setRestaurantId(order.getRestaurantId());
        response.setRestaurantName(order.getRestaurantName());
        response.setDeliveryAddress(order.getDeliveryAddress());
        response.setStatus(order.getStatus().name());
        response.setSubtotal(order.getSubtotal());
        response.setDeliveryFee(order.getDeliveryFee());
        response.setTax(order.getTax());
        response.setTotal(order.getTotal());
        response.setPaymentMethod(order.getPaymentMethod().name());
        response.setPaymentStatus(order.getPaymentStatus().name());
        response.setDeliveryPersonId(order.getDeliveryPersonId());
        response.setEstimatedDeliveryTime(order.getEstimatedDeliveryTime());
        response.setActualDeliveryTime(order.getActualDeliveryTime());
        response.setSpecialInstructions(order.getSpecialInstructions());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());

        // Convert order items
        List<OrderItemResponse> items = order.getOrderItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getId(),
                        item.getMenuItemId(),
                        item.getMenuItemName(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getSubtotal(),
                        item.getSpecialRequests()
                ))
                .collect(Collectors.toList());
        response.setItems(items);

        return response;
    }
}
