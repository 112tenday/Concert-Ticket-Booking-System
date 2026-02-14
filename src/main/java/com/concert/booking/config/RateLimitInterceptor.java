package com.concert.booking.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private final Map<String, Long> requestTimestamps = new ConcurrentHashMap<>();

    private static final int MAX_REQUESTS_PER_MINUTE = 100;
    private static final long ONE_MINUTE_IN_MILLIS = 60000;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = request.getRemoteAddr();
        long currentTime = System.currentTimeMillis();

        requestCounts.putIfAbsent(clientIp, new AtomicInteger(0));
        requestTimestamps.putIfAbsent(clientIp, currentTime);

        if (currentTime - requestTimestamps.get(clientIp) > ONE_MINUTE_IN_MILLIS) {
            requestCounts.get(clientIp).set(0);
            requestTimestamps.put(clientIp, currentTime);
        }

        if (requestCounts.get(clientIp).incrementAndGet() > MAX_REQUESTS_PER_MINUTE) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many requests. Max 100 requests per minute per IP.");
            return false;
        }

        return true;
    }
}