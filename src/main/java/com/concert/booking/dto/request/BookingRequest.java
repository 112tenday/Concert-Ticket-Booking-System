package com.concert.booking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingRequest {
    @NotNull(message = "Concert ID is required")
    private Long concertId;

    @NotNull(message = "Ticket Category ID is required")
    private Long ticketCategoryId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Idempotency Key must fill")
    private String idempotencyKey;
}