package com.concert.booking.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ConcertRequest {
    @NotBlank(message = "Concert name is required")
    private String name;

    @NotBlank(message = "Artist name is required")
    private String artistName;

    @NotBlank(message = "Venue is required")
    private String venue;

    @NotNull(message = "Concert date is required")
    @Future(message = "Concert date must be in the future")
    private LocalDateTime concertDate;

    @NotNull(message = "Base price is required")
    @Min(value = 1, message = "Price must be greater than 0")
    private BigDecimal basePrice;

    @NotNull(message = "Total capacity is required")
    @Min(value = 1, message = "Capacity must be greater than 0")
    private Integer totalCapacity;
}