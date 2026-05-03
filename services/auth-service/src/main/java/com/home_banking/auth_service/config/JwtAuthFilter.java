package com.home_banking.auth_service.config;

import com.home_banking.auth_service.entity.User;
import com.home_banking.auth_service.service.IJwtService;
import com.home_banking.auth_service.service.IUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
JwtAuthFilter - Validation Service which checks for correct existing jwts from requests

Fires JwtAuthFilter by extending OncePerRequestFilter everytime the user makes an http request.
This makes sure the filter checks the request for different methods all the time.
*/
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final IJwtService jwtService;
    private final IUserDetailService userDetailService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        // SecurityContextHolder == null, shows that the user is not already authenticated: therefore skip the authentication process
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userDetailService.loadUserByUsername(userEmail);
        }
    }
}
