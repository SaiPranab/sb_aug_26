package com.qcommerce.backend.security.jwt;

import com.qcommerce.backend.entity.UserEntity;
import com.qcommerce.backend.exception.ResourceNotFoundException;
import com.qcommerce.backend.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Authorization header
        String authHeader = request.getHeader("Authorization");

        // validate Authorization Header
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            // if validation failed :- skip the JWT filter
            // either request will hit a endpoint (permitAll) or rejected by Security Filter Chain due to authorization constraints
            filterChain.doFilter(request, response);
            return;
        }

        // if successfully validated means Jwt token is present
        String token = authHeader.substring(7);

        try{
            // is token expired
            if(jwtService.isTokenExpired(token)){
                throw new JwtException("Token is expired");
            }

            // extract the user id from token
            String userId = jwtService.extractUserId(token);

            if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getUserRole().name()));

                // Authentication Object
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);

                // Store the Authentication Object in the Security Context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JwtException e) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
