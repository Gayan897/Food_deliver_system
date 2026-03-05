package com.fooddelivery.food_delivery.controller;

import com.fooddelivery.food_delivery.dto.LoginRequest;
import com.fooddelivery.food_delivery.dto.UserRequest;
import com.fooddelivery.food_delivery.dto.UserResponse;
import com.fooddelivery.food_delivery.entity.User;
import com.fooddelivery.food_delivery.security.JwtUtil;
import com.fooddelivery.food_delivery.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequest request) {
        try {
            UserResponse user = userService.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            System.out.println("Login attempt for email: " + request.getEmail());

            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            System.out.println("Authentication successful");

            // Get user details
            User user = userService.getUserByEmail(request.getEmail());
            System.out.println("User found: " + user.getName() + ", Role: " + user.getRole());

            // Generate JWT token
            String token = jwtUtil.generateToken(user.getEmail());

            // Create response with correct structure
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);

            // Create user object
            Map<String, Object> userData = new HashMap<>();
            userData.put("userId", user.getId());
            userData.put("name", user.getName());
            userData.put("email", user.getEmail());
            userData.put("phone", user.getPhone());
            userData.put("role", user.getRole().toString());

            // Add user object to response
            response.put("user", userData);

            System.out.println("Sending response: " + response);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            UserResponse user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        try {
            UserResponse user = userService.updateUser(id, request);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}