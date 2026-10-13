package com.qcommerce.backend.security;

import com.qcommerce.backend.entity.UserEntity;
import com.qcommerce.backend.exception.ResourceNotFoundException;
import com.qcommerce.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email not Found"));

        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(userEntity.getUserRole().name());

        return User.builder()
                .username(userEntity.getUserEmail())
                .password(userEntity.getUserPassword())
                .disabled(!userEntity.isEnabled())
                .roles(authority.getAuthority())
                .build();
    }
}
