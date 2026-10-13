package com.qcommerce.backend.service;

import com.qcommerce.backend.dto.request.LoginRequest;
import com.qcommerce.backend.dto.request.RegisterRequest;
import com.qcommerce.backend.dto.response.UserResponse;
import com.qcommerce.backend.entity.UserEntity;
import com.qcommerce.backend.exception.DuplicateEntryException;
import com.qcommerce.backend.exception.ResourceNotFoundException;
import com.qcommerce.backend.mapper.UserMapper;
import com.qcommerce.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository contextRepository;

    public UserResponse registerUser(RegisterRequest request) {
        if(userRepository.existsByUserEmail(request.userEmail())){
            throw new DuplicateEntryException("Email already registered with us");
        }

        UserEntity user = UserMapper.toEntity(request, null);

        // Create empty cart when an user is registerd - Future Work

        UserEntity savedUser = userRepository.save(user);
        return UserMapper.toResponse(savedUser);
    }

    public UserResponse loginUser(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());

        authenticationManager.authenticate(authentication);

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
        contextRepository.saveContext(context, request, response);

        UserEntity userEntity = userRepository.findByUserEmail(loginRequest.email())
                .orElseThrow(() -> new ResourceNotFoundException("Email not Found"));

        return UserMapper.toResponse(userEntity);
    }
}
