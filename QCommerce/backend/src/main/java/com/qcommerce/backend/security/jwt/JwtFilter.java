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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
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

            if(userId != null){
                UserEntity user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

                if(!user.isEnabled()) {
                    sendResponse(response, HttpStatus.UNAUTHORIZED, "user is disabled. Contact Admin");
                    return;
                }

                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getUserRole().name()));

                // Authentication Object
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);

                // Store the Authentication Object in the Security Context
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // HttpServletRequest -> Attribute -> userId
                request.setAttribute("userId", user.getUserId());
            }
        } catch (JwtException e) {
            SecurityContextHolder.clearContext();
            sendResponse(response, HttpStatus.UNAUTHORIZED, e.getMessage());
            return;
        } catch (Exception e){
            sendResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private void sendResponse(HttpServletResponse response, HttpStatus status, String details) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String jsonBody = """
                {
                    "title": "%s",
                    "status": %d,
                    "details": "%s",
                    "timestamp": "%s",
                }
                """.formatted(status.getReasonPhrase(), status.value(), details, LocalDateTime.now());

        response.getWriter().write(jsonBody);
    }
}

/**
 *
 * Authentication :- Who are you ? -> 401 (Unauthorized)
 * Authorization :- What roles you have ? -> 403 (Forbidden)
 *
 */