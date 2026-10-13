package com.qcommerce.backend.controller;

import com.qcommerce.backend.dto.request.LoginRequest;
import com.qcommerce.backend.dto.request.RegisterRequest;
import com.qcommerce.backend.dto.response.UserResponse;
import com.qcommerce.backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody @Valid RegisterRequest request) {
        return authService.registerUser(request);
    }

    @PostMapping("/login")
    public UserResponse login(@RequestBody @Valid LoginRequest loginRequest,
                              HttpServletRequest request, HttpServletResponse response) {
        return authService.loginUser(loginRequest, request, response);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout() {
        SecurityContextHolder.clearContext();
    }
}
