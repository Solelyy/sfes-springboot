package com.sfes.security.jwt;

import com.sfes.security.user.CustomUserDetailsService;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customerUserDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = null;
        String email = null;

        // 1. Find JWT in cookies
        if (request.getCookies() != null) {

            for (Cookie cookie : request.getCookies()) {

                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // 2. Only attempt authentication if a token exists
        if (token != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            try {

                // 3. Extract email from JWT
                email = jwtService.extractEmail(token);

                // 4. Validate JWT
                if (email != null &&
                        jwtService.isTokenValid(token, email)) {
                    ;

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
