package com.qcommerce.backend.service;

import com.qcommerce.backend.dto.request.LoginRequest;
import com.qcommerce.backend.dto.request.RegisterRequest;
import com.qcommerce.backend.dto.response.LoginResponse;
import com.qcommerce.backend.dto.response.UserResponse;
import com.qcommerce.backend.entity.UserEntity;
import com.qcommerce.backend.exception.DuplicateEntryException;
import com.qcommerce.backend.exception.ResourceNotFoundException;
import com.qcommerce.backend.mapper.UserMapper;
import com.qcommerce.backend.repository.UserRepository;
import com.qcommerce.backend.security.jwt.JwtService;
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
    private final JwtService jwtService;

    public UserResponse registerUser(RegisterRequest request) {
        if(userRepository.existsByUserEmail(request.userEmail())){
            throw new DuplicateEntryException("Email already registered with us");
        }

        UserEntity user = UserMapper.toEntity(request, passwordEncoder);

        // Create empty cart when an user is registerd - Future Work

        UserEntity savedUser = userRepository.save(user);
        return UserMapper.toResponse(savedUser);
    }

    public LoginResponse loginUser(LoginRequest loginRequest) {
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());

        authenticationManager.authenticate(authentication);

        UserEntity userEntity = userRepository.findByUserEmail(loginRequest.email())
                .orElseThrow(() -> new ResourceNotFoundException("Email not Found"));

        // generate token
        String token = jwtService.generateToken(userEntity.getUserId(), userEntity.getUserRole().name());

        // return response
        return new LoginResponse(token);
    }
}
