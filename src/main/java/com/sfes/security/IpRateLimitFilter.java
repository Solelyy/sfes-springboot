package com.sfes.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.github.bucket4j.Bucket;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class IpRateLimitFilter extends OncePerRequestFilter {
    private final RateLimitProperties rateLimitProperties;

    private final Cache<String, Bucket> cache = Caffeine.newBuilder()
            .expireAfterAccess(15, TimeUnit.MINUTES)
            .build();

    private Bucket createBucket(RateLimitProperties.BucketConfig config) {
        return  Bucket.builder()
                .addLimit(limit -> limit
                        .capacity(config.capacity())
                        .refillIntervally(config.refill(), Duration.ofMinutes(1))
                )
                .build();
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String ip = request.getRemoteAddr();
        String bucketType = resolveBucketType(request);

        RateLimitProperties.BucketConfig config =
                rateLimitProperties.limits().get(bucketType);

        String cacheKey = ip + ":" + bucketType;

        Bucket bucket = cache.get(
                cacheKey,
                k -> createBucket(config)
        );

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many requests. Please try again later.");
        }
    }

    private String resolveBucketType(HttpServletRequest request){
        String uri = request.getRequestURI();
        String method = request.getMethod();

        if (method.equals("POST") && uri.equals("/auth/login")){
            return "login";
        }

        if (method.equals("POST") && uri.equals("/password-resets/email")){
            return "password-reset";
        }

        return "general";
    }
}
