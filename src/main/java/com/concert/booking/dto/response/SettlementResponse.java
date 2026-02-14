package com.concert.booking.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SettlementResponse {
    private Long concertId;
    private String concertName;
    private BigDecimal totalRevenue;
    private Integer totalTicketsSold;
    private Double occupancyRate;
    private List<TransactionDetail> payments;

    @Data
    @Builder
    public static class TransactionDetail {
        private Long bookingId;
        private Long userId;
        private BigDecimal amount;
        private LocalDateTime timestamp;
    }
}