package com.qcommerce.backend.service;

import com.qcommerce.backend.dto.request.RegisterRequest;
import com.qcommerce.backend.dto.response.UserResponse;
import com.qcommerce.backend.entity.UserEntity;
import com.qcommerce.backend.exception.DuplicateEntryException;
import com.qcommerce.backend.mapper.UserMapper;
import com.qcommerce.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;

    public UserResponse registerUser(RegisterRequest request) {
        if(userRepository.existsByUserEmail(request.userEmail())){
            throw new DuplicateEntryException("Email already registered with us");
        }

        UserEntity user = UserMapper.toEntity(request, null);

        // Create empty cart when an user is registerd - Future Work

        UserEntity savedUser = userRepository.save(user);
        return UserMapper.toResponse(savedUser);
    }
}
