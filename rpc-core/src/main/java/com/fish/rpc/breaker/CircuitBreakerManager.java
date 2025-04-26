package com.fish.rpc.breaker;

import com.fish.rpc.annotation.Breaker;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Afish
 * @date 2025/4/26 15:42
 */
public class CircuitBreakerManager {
    private static final Map<String, CircuitBreaker> BREAKER_MAP = new HashMap<>();

    public static CircuitBreaker get(String key, Breaker breaker) {
        return BREAKER_MAP.computeIfAbsent(key, __ -> new CircuitBreaker(
                breaker.failThreshold(),
                breaker.successRateInHalfOpen(),
                breaker.windowTime()
        ));
    }
}
