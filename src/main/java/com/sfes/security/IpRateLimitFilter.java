package com.sfes.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.github.bucket4j.Bucket;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class IpRateLimitFilter extends OncePerRequestFilter {
    @Value("${bucket.capacity}")
    private long bucketCapacity;

    @Value("${bucket.refill}")
    private long bucketRefill;

    private final Cache<String, Bucket> cache = Caffeine.newBuilder()
            .expireAfterAccess(15, TimeUnit.MINUTES)
            .build();

    private Bucket createBucket() {
        return  Bucket.builder()
                .addLimit(limit -> limit
                        .capacity(bucketCapacity)
                        .refillIntervally(bucketRefill, Duration.ofMinutes(1))
                )
                .build();
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();

        log.debug("IP Address: {}", ip);

        if (!uri.equals("/api/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        Bucket bucket = cache.get(ip, k -> createBucket());
        log.debug("""
                IP: {}
                Bucket: {}
                """, ip, bucket);

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many requests. Please try again later.");
        }
    }
}
