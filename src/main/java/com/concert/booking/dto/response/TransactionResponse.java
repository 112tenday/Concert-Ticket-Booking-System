package com.concert.booking.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponse {
    private Long transactionId;
    private Long concertId;
    private Long userId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime timestamp;
}