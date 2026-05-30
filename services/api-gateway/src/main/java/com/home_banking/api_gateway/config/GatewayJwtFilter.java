package com.home_banking.api_gateway.config;

import com.home_banking.api_gateway.service.GatewayJwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class GatewayJwtFilter extends OncePerRequestFilter {
    private final GatewayJwtService gatewayJwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try{
            String token = authHeader.substring(7);
            Claims claims = gatewayJwtService.extractAllClaims(token);
            String subject = claims.getSubject();
            Long userId = claims.get("userId", Long.class);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(subject, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(auth);

            HttpServletRequest enrichedRequest = new UserIdHeaderRequestWrapper(request, userId);
            filterChain.doFilter(enrichedRequest, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/api/v1/auth/")
                || uri.startsWith("/swagger-ui")
                || uri.startsWith("/v3/api-docs")
                || uri.startsWith("/webjars")
                || uri.endsWith("/v3/api-docs");
    }

    private static class UserIdHeaderRequestWrapper extends HttpServletRequestWrapper {
        private final String userId;

        UserIdHeaderRequestWrapper(HttpServletRequest request, Long userId) {
            super(request);
            this.userId = userId != null ? userId.toString() : null;
        }

        @Override
        public String getHeader(String name) {
            // Prevents header injection so the x-user-id comes only from the validated jwt
            if ("X-User-Id".equalsIgnoreCase(name)) return userId;
            return super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            if ("X-User-Id".equalsIgnoreCase(name) && userId != null) {
                return Collections.enumeration(List.of(userId));
            }
            return super.getHeaders(name);
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            List<String> names = Collections.list(super.getHeaderNames());
            if (userId != null) names.add("X-User-Id");
            return Collections.enumeration(names);
        }
    }
}
