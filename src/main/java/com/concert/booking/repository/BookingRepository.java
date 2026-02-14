package com.concert.booking.repository;

import com.concert.booking.entity.Booking;
import com.concert.booking.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {


//    Optional<Booking> findByIdempotencyKey(String idempotencyKey);
    boolean existsByIdempotencyKey(String idempotencyKey);
    List<Booking> findByStatusAndExpiresAtBefore(BookingStatus status, LocalDateTime now);
    List<Booking> findByUserId(Long userId);
    List<Booking> findByConcertId(Long concertId);
}