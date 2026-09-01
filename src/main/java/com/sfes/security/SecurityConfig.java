package com.sfes.security;

import com.sfes.security.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthenticationFilter;
    private final IpRateLimitFilter ipRateLimitFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // 1. Disable CSRF
                .csrf(AbstractHttpConfigurer::disable)

                // 2. CORS config for frontend
                .cors(cors -> cors.configurationSource(request -> {

                    CorsConfiguration config = new CorsConfiguration();

                    config.setAllowedOrigins(List.of(
                            "http://localhost:5173"
                    ));

                    config.setAllowedMethods(List.of(
                            "GET",
                            "POST",
                            "PUT",
                            "DELETE",
                            "PATCH"
                    ));

                    config.setAllowedHeaders(List.of("*"));

                    // allow cookies (JWT HttpOnly cookie)
                    config.setAllowCredentials(true);

                    return config;
                }))

                // 3. Session disabled
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 4. Route protection rules
                .authorizeHttpRequests(auth -> auth
                        // public endpoints (auth)
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/superadmin/**").hasAuthority("SUPER_ADMIN")
                        // everything else requires authentication
                        .anyRequest().authenticated()
                )

                // 5. Disable default Spring login form
                .formLogin(AbstractHttpConfigurer::disable)

                // 6. Disable HTTP Basic auth
                .httpBasic(AbstractHttpConfigurer::disable)

                //7. Add IP rate limit filter before Spring username/password authentication filter
                .addFilterBefore(
                        ipRateLimitFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                // 8. Add JWT filter before IP rate limit filter
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        IpRateLimitFilter.class
                );

        return http.build();
    }

    // for hashing passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
