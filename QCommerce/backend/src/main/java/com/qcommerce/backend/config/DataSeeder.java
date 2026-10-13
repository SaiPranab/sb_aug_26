package com.qcommerce.backend.config;

import com.qcommerce.backend.entity.UserEntity;
import com.qcommerce.backend.entity.helper.AddressInfo;
import com.qcommerce.backend.entity.helper.ContactInfo;
import com.qcommerce.backend.entity.helper.Role;
import com.qcommerce.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.count() <= 0){
            UserEntity adminUser = UserEntity.builder()
                    .userEmail("admin@gmail.com")
                    .userPassword(passwordEncoder.encode("admin"))
                    .userRole(Role.ADMIN)
                    .profileImage(null)
                    .enabled(true)
                    .contactInfo(new ContactInfo("admin admin", "999999999"))
                    .addressInfo(new AddressInfo("", "", "", ""))
                    .build();

            UserEntity normalUser = UserEntity.builder()
                    .userEmail("user@gmail.com")
                    .userPassword(passwordEncoder.encode("user"))
                    .userRole(Role.USER)
                    .profileImage("")
                    .enabled(true)
                    .contactInfo(new ContactInfo("Default User", "999999999"))
                    .addressInfo(new AddressInfo("", "", "", ""))
                    .build();

            userRepository.saveAll(List.of(adminUser, normalUser));
        }
    }
}
