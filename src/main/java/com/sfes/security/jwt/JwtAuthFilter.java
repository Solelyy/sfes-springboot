package com.sfes.security.jwt;

import com.sfes.auth.service.RedisService;
import com.sfes.security.user.CustomUserDetailsService;
import com.sfes.user.entity.User;
import com.sfes.user.enums.Status;
import com.sfes.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customerUserDetailsService;
    private final UserRepository userRepository;
    private final RedisService redisService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = null;

        // 1. Find jwt in cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {

                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // 2. Only attempt authentication if a token exists
        if (token != null && !token.isBlank() &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            try {
                Claims claims = jwtService.extractAllClaims(token);

                // 3. Extract email from JWT & jti
                String email = claims.getSubject();
                String jti = claims.get("jti", String.class);
                Integer jwtVersion = claims.get("tokenVersion", Integer.class);

                if (email == null || jti == null || jwtVersion == null) {
                    throw new JwtException("Required JWT claims are missing");
                }

                User user = userRepository.findByEmail(email)
                        .orElse(null);

                // 4. Validate token version, status, and check if the token is blacklisted in Redis
                if (user != null
                        && Objects.equals(user.getTokenVersion(), jwtVersion)
                        && user.getStatus() == Status.ACTIVE
                        && !redisService.exists("revoked:" + jti)
                ) {

                    // 5. Load Spring Security UserDetails
                    var userDetails =
                            customerUserDetailsService
                                    .loadUserByUsername(email);

                    // 6. Create Authentication
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    // 7. Attach request details
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    // 8. Store authentication
                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authToken);
                }

            } catch (Exception e) {
                log.warn("JWT auth failed: {}", e);
                SecurityContextHolder.clearContext();
            }
        }

        // 9. Continue through Spring Security's filter chain
        filterChain.doFilter(request, response);
    }
}
