package com.fooddelivery.food_delivery.service;

import com.fooddelivery.food_delivery.dto.*;
import com.fooddelivery.food_delivery.entity.User;
import com.fooddelivery.food_delivery.repository.UserRepository;
import com.fooddelivery.food_delivery.security.JwtUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Transactional
    public UserResponse registerUser(@Valid UserRequest request) {
        System.out.println(" UserService: Registration attempt for: " + request.getEmail());

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            System.err.println(" UserService: Email already exists: " + request.getEmail());
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // ✅ CRITICAL: Encode the password before saving
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodedPassword);

        System.out.println(" UserService: Password encoded successfully");
        System.out.println("   Plain password length: " + request.getPassword().length());
        System.out.println("   Encoded password (first 20 chars): " + encodedPassword.substring(0, 20) + "...");

        user.setPhone(request.getPhone());
        user.setRole(User.Role.valueOf(request.getRole().toUpperCase()));
        user.setIsActive(true);

        User savedUser = userRepository.save(user);

        System.out.println(" UserService: User registered successfully with ID: " + savedUser.getId());

        return convertToResponse(savedUser);
    }

    public JwtResponse loginUser(LoginRequest request) {
        System.out.println(" UserService: Login attempt for: " + request.getEmail());

        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            System.out.println(" UserService: Authentication successful for: " + request.getEmail());

        } catch (BadCredentialsException e) {
            System.err.println(" UserService: Invalid credentials for: " + request.getEmail());
            throw new RuntimeException("Invalid email or password");
        } catch (Exception e) {
            System.err.println(" UserService: Authentication error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }

        // Get user details
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail());

        System.out.println("🎫 UserService: JWT token generated for: " + user.getEmail());

        return new JwtResponse(
                token,
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole().name()
        );
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToResponse(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, @Valid UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(request.getName());
        user.setPhone(request.getPhone());

        // Only update password if provided
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return convertToResponse(updatedUser);
    }

    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole().name());
        response.setIsActive(user.getIsActive());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }

    public User getUserByEmail(String email) {
        System.out.println("🔍 UserService: Looking up user by email: " + email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        System.out.println("✅ UserService: User found - Name: " + user.getName() + ", Role: " + user.getRole());
        return user;
    }



}
