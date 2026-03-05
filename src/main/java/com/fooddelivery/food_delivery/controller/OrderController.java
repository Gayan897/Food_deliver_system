package com.fooddelivery.food_delivery.controller;

import com.fooddelivery.food_delivery.dto.OrderItemResponse;
import com.fooddelivery.food_delivery.dto.OrderResponse;
import com.fooddelivery.food_delivery.entity.Order;
import com.fooddelivery.food_delivery.entity.OrderItem;
import com.fooddelivery.food_delivery.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    // ✅ GET ALL ORDERS - Returns DTOs
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        try {
            List<Order> orders = orderRepository.findAll();
            List<OrderResponse> orderResponses = orders.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            System.out.println("📦 Returning " + orderResponses.size() + " orders");
            return ResponseEntity.ok(orderResponses);
        } catch (Exception e) {
            System.err.println("❌ Error fetching orders: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // ✅ GET ORDER BY ID - Returns DTO
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        try {
            Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            OrderResponse response = convertToDTO(order);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ CREATE ORDER
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        try {
            System.out.println("➕ Creating order for user: " + order.getUserId());

            Order savedOrder = orderRepository.save(order);
            OrderResponse response = convertToDTO(savedOrder);

            System.out.println("✅ Order created with ID: " + savedOrder.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            System.err.println("❌ Error creating order: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ UPDATE ORDER STATUS
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        try {
            System.out.println("🔄 Updating order " + id + " status to: " + status);

            Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            order.setStatus(Order.OrderStatus.valueOf(status));
            Order updatedOrder = orderRepository.save(order);

            OrderResponse response = convertToDTO(updatedOrder);

            System.out.println("✅ Order status updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ Error updating status: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ GET ORDERS BY USER - Returns DTOs
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByUserId(@PathVariable Long userId) {
        try {
            System.out.println("📦 Getting orders for user: " + userId);

            List<Order> orders = orderRepository.findByUserId(userId);
            List<OrderResponse> orderResponses = orders.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            System.out.println("✅ Found " + orderResponses.size() + " orders for user " + userId);
            return ResponseEntity.ok(orderResponses);
        } catch (Exception e) {
            System.err.println("❌ Error fetching user orders: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // ✅ GET ORDER STATISTICS
    @GetMapping("/stats")
    public ResponseEntity<?> getOrderStats() {
        try {
            Map<String, Object> stats = new HashMap<>();

            List<Order> allOrders = orderRepository.findAll();

            stats.put("totalOrders", allOrders.size());

            long activeOrders = allOrders.stream()
                    .filter(o -> o.getStatus() != Order.OrderStatus.DELIVERED &&
                            o.getStatus() != Order.OrderStatus.CANCELLED)
                    .count();
            stats.put("activeOrders", activeOrders);

            long pendingOrders = allOrders.stream()
                    .filter(o -> o.getStatus() == Order.OrderStatus.PENDING)
                    .count();
            stats.put("pendingOrders", pendingOrders);

            BigDecimal totalRevenue = allOrders.stream()
                    .filter(o -> o.getStatus() == Order.OrderStatus.DELIVERED)
                    .map(Order::getTotal)
                    .filter(total -> total != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            stats.put("totalRevenue", totalRevenue.doubleValue());

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ GET RECENT ORDERS - Returns DTOs
    @GetMapping("/recent")
    public ResponseEntity<?> getRecentOrders(@RequestParam(defaultValue = "5") int limit) {
        try {
            List<Order> orders = orderRepository.findAll();

            List<OrderResponse> recentOrders = orders.stream()
                    .sorted((a, b) -> {
                        if (a.getCreatedAt() == null) return 1;
                        if (b.getCreatedAt() == null) return -1;
                        return b.getCreatedAt().compareTo(a.getCreatedAt());
                    })
                    .limit(limit)
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(recentOrders);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ DELETE ORDER
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            System.out.println("🗑️ Deleting order: " + id);

            Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            orderRepository.delete(order);
            System.out.println("✅ Order deleted successfully");

            return ResponseEntity.ok(Map.of("message", "Order deleted successfully"));
        } catch (Exception e) {
            System.err.println("❌ Error deleting order: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ HELPER METHOD: Convert Order Entity to DTO
    private OrderResponse convertToDTO(Order order) {
        OrderResponse dto = new OrderResponse();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setRestaurantId(order.getRestaurantId());
        dto.setRestaurantName(order.getRestaurantName());
        dto.setDeliveryAddress(order.getDeliveryAddress());
        dto.setStatus(order.getStatus() != null ? order.getStatus().toString() : "PENDING");
        dto.setSubtotal(order.getSubtotal());
        dto.setDeliveryFee(order.getDeliveryFee());
        dto.setTax(order.getTax());
        dto.setTotal(order.getTotal());
        dto.setPaymentMethod(order.getPaymentMethod() != null ? order.getPaymentMethod().toString() : null);
        dto.setPaymentStatus(order.getPaymentStatus() != null ? order.getPaymentStatus().toString() : "PENDING");
        dto.setDeliveryPersonId(order.getDeliveryPersonId());
        dto.setEstimatedDeliveryTime(order.getEstimatedDeliveryTime());
        dto.setActualDeliveryTime(order.getActualDeliveryTime());
        dto.setSpecialInstructions(order.getSpecialInstructions());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());

        // Convert OrderItems to OrderItemResponse DTOs
        if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
            List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                    .map(this::convertItemToDTO)
                    .collect(Collectors.toList());
            dto.setItems(itemResponses);
        }

        return dto;
    }

    // ✅ HELPER METHOD: Convert OrderItem Entity to DTO
    private OrderItemResponse convertItemToDTO(OrderItem item) {
        OrderItemResponse dto = new OrderItemResponse();
        dto.setId(item.getId());
        dto.setMenuItemId(item.getMenuItemId());
        dto.setMenuItemName(item.getMenuItemName());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        dto.setSubtotal(item.getSubtotal());
        dto.setSpecialRequests(item.getSpecialRequests());
        return dto;
    }
}