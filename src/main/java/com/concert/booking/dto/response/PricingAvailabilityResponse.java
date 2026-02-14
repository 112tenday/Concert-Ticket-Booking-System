package com.concert.booking.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PricingAvailabilityResponse {
    private Long categoryId;
    private String categoryName;
    private BigDecimal basePrice;
    private BigDecimal currentPrice;
    private Integer availableStock;
    private Integer totalCapacity;
}