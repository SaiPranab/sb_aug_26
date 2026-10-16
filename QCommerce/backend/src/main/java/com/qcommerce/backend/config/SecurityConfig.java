package com.qcommerce.backend.config;

import com.qcommerce.backend.entity.helper.Role;
import com.qcommerce.backend.security.CustomUserDetailsService;
import com.qcommerce.backend.security.jwt.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
//                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                // AuthenticationEntryPoint -> fires when a request is not authenticated (no token)
                // AccessDeniedHandler -> When request is authenticated but lacks the role
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            String jsonResponse = """
                                        {
                                            "status" : "401",
                                            "Title" : "Unauthorized",
                                            "message" : "%s",
                                            "timestamp" : "%s"
                                        }
                                    """.formatted(authException.getMessage(), LocalDateTime.now());
                            response.getWriter().write(jsonResponse);
                        })
                        .accessDeniedHandler((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            String jsonResponse = """
                                        {
                                            "status" : "403",
                                            "Title" : "Forbidden",
                                            "message" : "Insufficient role to access this resource",
                                            "timestamp" : "%s"
                                        }
                                    """.formatted(LocalDateTime.now());
                            response.getWriter().write(jsonResponse);
                        })

                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/category/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/category/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/api/category/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/category/**").hasRole(Role.ADMIN.name())

                        .requestMatchers(HttpMethod.GET, "/api/items/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/items/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/api/items/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/items/**").hasRole(Role.ADMIN.name())

                        .requestMatchers("/api/users/**").authenticated()
                        .requestMatchers("/api/admin/**").hasRole(Role.ADMIN.name())

                        .anyRequest().authenticated()
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
}
