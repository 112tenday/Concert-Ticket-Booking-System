package com.concert.booking.dto.response;

import com.concert.booking.model.BookingStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingResponse {
    private Long bookingId;
    private Long userId;
    private Long concertId;
    private String ticketCategory;
    private BigDecimal totalAmount;
    private BookingStatus status;
    private LocalDateTime bookingDate;
    private LocalDateTime expiresAt;
}