package com.fooddelivery.food_delivery.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Skip JWT processing for OPTIONS preflight requests
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");

        // DEBUG - remove after fixing
        System.out.println("=== JWT FILTER === Path: " + request.getRequestURI());
        System.out.println("Auth Header present: " + (authorizationHeader != null));

        String username = null;
        String jwt = null;

        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                try {
                    username = jwtUtil.extractUsername(jwt);
                    System.out.println("Extracted username: " + username); // DEBUG
                } catch (Exception e) {
                    // Token is malformed or expired — log and continue unauthenticated
                    System.out.println("JWT extraction failed: " + e.getMessage()); // DEBUG
                    logger.warn("JWT Token extraction failed: " + e.getMessage());
                }
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (jwtUtil.validateToken(jwt, userDetails)) {
                    System.out.println("Token VALID, authenticating user: " + username); // DEBUG
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    System.out.println("Token INVALID for user: " + username); // DEBUG
                }
            }

        } catch (Exception e) {
            // Catch-all: never let a filter exception block the request
            // For permitAll() endpoints this lets the request through unauthenticated
            logger.error("Unexpected error in JWT filter: " + e.getMessage(), e);
        }

        // Always continue the filter chain — security rules decide access, not this filter
        filterChain.doFilter(request, response);
    }
}