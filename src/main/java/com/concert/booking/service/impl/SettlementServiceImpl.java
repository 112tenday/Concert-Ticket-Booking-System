package com.concert.booking.service.impl;

import com.concert.booking.dto.response.DashboardResponse;
import com.concert.booking.dto.response.SettlementResponse;
import com.concert.booking.entity.Booking;
import com.concert.booking.entity.Concert;
import com.concert.booking.entity.TicketCategory;
import com.concert.booking.model.BookingStatus;
import com.concert.booking.repository.BookingRepository;
import com.concert.booking.repository.ConcertRepository;
import com.concert.booking.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SettlementServiceImpl implements SettlementService {

    private final ConcertRepository concertRepository;
    private final BookingRepository bookingRepository;

    @Override
    public SettlementResponse getSettlementReport(Long concertId) {
        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(() -> new RuntimeException("Concert not found with id: " + concertId));

        List<Booking> validBookings = bookingRepository.findByConcertId(concertId).stream()
                .filter(b -> b.getStatus() != BookingStatus.CANCELLED)
                .toList();

        BigDecimal totalRevenue = validBookings.stream()
                .map(Booking::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalTicketsSold = validBookings.size();
        int totalCapacity = concert.getTicketCategories().stream()
                .mapToInt(TicketCategory::getTotalAllocation)
                .sum();

        double occupancyRate = totalCapacity > 0 ? ((double) totalTicketsSold / totalCapacity) * 100 : 0.0;

        List<SettlementResponse.TransactionDetail> payments = validBookings.stream()
                .map(b -> SettlementResponse.TransactionDetail.builder()
                        .bookingId(b.getId())
                        .userId(b.getUserId())
                        .amount(b.getTotalAmount())
                        .timestamp(b.getBookingDate())
                        .build())
                .toList();

        return SettlementResponse.builder()
                .concertId(concert.getId())
                .concertName(concert.getName())
                .totalRevenue(totalRevenue)
                .totalTicketsSold(totalTicketsSold)
                .occupancyRate(occupancyRate)
                .payments(payments)
                .build();
    }
    @Override
    public DashboardResponse getDashboardAnalytics() {
        List<Booking> allValidBookings = bookingRepository.findAll().stream()
                .filter(b -> b.getStatus() != BookingStatus.CANCELLED)
                .toList();

        BigDecimal totalRevenue = allValidBookings.stream()
                .map(Booking::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalTicketsSold = allValidBookings.size();

        int totalCapacityAllConcerts = concertRepository.findAll().stream()
                .flatMap(c -> c.getTicketCategories().stream())
                .mapToInt(TicketCategory::getTotalAllocation)
                .sum();

        double overallOccupancyRate = totalCapacityAllConcerts > 0
                ? ((double) totalTicketsSold / totalCapacityAllConcerts) * 100
                : 0.0;

        return DashboardResponse.builder()
                .totalRevenue(totalRevenue)
                .totalTicketsSold(totalTicketsSold)
                .overallOccupancyRate(overallOccupancyRate)
                .build();
    }
    @Override
    public java.util.List<com.concert.booking.dto.response.TransactionResponse> getAllTransactions() {
        return bookingRepository.findAll().stream()
                .map(b -> com.concert.booking.dto.response.TransactionResponse.builder()
                        .transactionId(b.getId())
                        .concertId(b.getConcertId())
                        .userId(b.getUserId())
                        .amount(b.getTotalAmount())
                        .status(b.getStatus().name())
                        .timestamp(b.getBookingDate())
                        .build())
                .toList();
    }
}