package com.concert.booking.service.impl;

import com.concert.booking.dto.request.BookingRequest;
import com.concert.booking.dto.response.BookingResponse;
import com.concert.booking.entity.Booking;
import com.concert.booking.entity.TicketCategory;
import com.concert.booking.model.BookingStatus;
import com.concert.booking.repository.BookingRepository;
import com.concert.booking.repository.TicketCategoryRepository;
import com.concert.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final TicketCategoryRepository ticketCategoryRepository;

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        if (request.getIdempotencyKey() != null) {
            boolean isDuplicate = bookingRepository.existsByIdempotencyKey(request.getIdempotencyKey());
            if (isDuplicate) {
                throw new RuntimeException("Duplicate request detected. A booking with this Idempotency Key already exists.");
            }
        }


        TicketCategory category = ticketCategoryRepository.findByIdWithLock(request.getTicketCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (category.getAvailableStock() <= 0) {
            throw new RuntimeException("Tickets sold out!");
        }

        BigDecimal finalPrice = calculateDynamicPrice(category);

        category.setAvailableStock(category.getAvailableStock() - 1);
        ticketCategoryRepository.save(category);

        Booking booking = new Booking();
        booking.setConcertId(request.getConcertId());
        booking.setTicketCategoryId(category.getId());
        booking.setUserId(request.getUserId());
        booking.setTotalAmount(finalPrice);
        booking.setStatus(BookingStatus.PENDING);
        booking.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        booking.setIdempotencyKey(request.getIdempotencyKey());

        Booking savedBooking = bookingRepository.save(booking);

        return BookingResponse.builder()
                .bookingId(savedBooking.getId())
                .userId(savedBooking.getUserId())
                .concertId(savedBooking.getConcertId())
                .ticketCategory(category.getName())
                .totalAmount(savedBooking.getTotalAmount())
                .status(savedBooking.getStatus())
                .expiresAt(savedBooking.getExpiresAt())
                .build();
    }

    private BigDecimal calculateDynamicPrice(TicketCategory category) {
        double availabilityPercentage = (double) category.getAvailableStock() / category.getTotalAllocation();

        double demandMultiplier = 0.0;

        if (availabilityPercentage <= 0.2) {
            demandMultiplier = 2.5;
        } else if (availabilityPercentage <= 0.4) {
            demandMultiplier = 1.5;
        } else if (availabilityPercentage <= 0.6) {
            demandMultiplier = 0.5;
        } else if (availabilityPercentage <= 0.8) {
            demandMultiplier = 0.1;
        } else {
            demandMultiplier = 0.0;
        }

        BigDecimal multiplierFactor = BigDecimal.valueOf(1 + demandMultiplier);
        return category.getBasePrice().multiply(multiplierFactor);
    }

    @Override
    public BookingResponse getBookingDetails(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        return mapToResponse(booking);
    }

    @Override
    public List<BookingResponse> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public void cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Only PENDING bookings can be cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        TicketCategory category = ticketCategoryRepository.findById(booking.getTicketCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setAvailableStock(category.getAvailableStock() + 1);
        ticketCategoryRepository.save(category);
    }

    private BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder()
                .bookingId(booking.getId())
                .userId(booking.getUserId())
                .concertId(booking.getConcertId())
                .status(booking.getStatus())
                .totalAmount(booking.getTotalAmount())
                .expiresAt(booking.getExpiresAt())
                .build();
    }
}