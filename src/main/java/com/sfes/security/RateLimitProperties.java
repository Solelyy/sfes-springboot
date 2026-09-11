package com.sfes.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "bucket")
public record RateLimitProperties (
        Map<String, BucketConfig> limits
){
    public record BucketConfig(
            Integer capacity,
            Integer refill
    ){}
}
