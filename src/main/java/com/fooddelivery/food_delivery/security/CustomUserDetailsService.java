package com.fooddelivery.food_delivery.security;

import com.fooddelivery.food_delivery.entity.User;
import com.fooddelivery.food_delivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println(" CustomUserDetailsService: Loading user by email: " + email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.err.println(" CustomUserDetailsService: User not found: " + email);
                    return new UsernameNotFoundException("User not found with email: " + email);
                });

        System.out.println(" CustomUserDetailsService: User found: " + user.getEmail());
        System.out.println(" CustomUserDetailsService: Password (first 20 chars): " +
                user.getPassword().substring(0, Math.min(20, user.getPassword().length())) + "...");

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}
