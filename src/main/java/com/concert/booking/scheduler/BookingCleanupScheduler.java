package com.concert.booking.scheduler;

import com.concert.booking.entity.Booking;
import com.concert.booking.entity.TicketCategory;
import com.concert.booking.model.BookingStatus;
import com.concert.booking.repository.BookingRepository;
import com.concert.booking.repository.TicketCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingCleanupScheduler {

    private final BookingRepository bookingRepository;
    private final TicketCategoryRepository ticketCategoryRepository;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cleanupExpiredBookings() {
        log.info("Starting cleanup of expired bookings...");

        List<Booking> expiredBookings = bookingRepository.findByStatusAndExpiresAtBefore(
                BookingStatus.PENDING, LocalDateTime.now());

        for (Booking booking : expiredBookings) {
            log.info("Expiring booking ID: {}", booking.getId());

            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);

            TicketCategory category = ticketCategoryRepository.findById(booking.getTicketCategoryId())
                    .orElse(null);

            if (category != null) {
                category.setAvailableStock(category.getAvailableStock() + 1);
                ticketCategoryRepository.save(category);
                log.info("Stock returned for category: {}", category.getName());
            }
        }
    }
}