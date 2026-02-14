package com.concert.booking.controller;

import com.concert.booking.dto.request.BookingRequest;
import com.concert.booking.dto.response.BookingResponse;
import com.concert.booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Tag(name = "2. Booking Management")
public class BookingController {

    private final BookingService bookingService;
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @RequestHeader(value = "X-Idempotency-Key", required = true) String idempotencyKey,
            @Valid @RequestBody BookingRequest request) {

        request.setIdempotencyKey(idempotencyKey);

        BookingResponse response = bookingService.createBooking(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingDetails(id));
    }

    @GetMapping
    public ResponseEntity<List<BookingResponse>> listUserBookings(@RequestParam Long userId) {
        return ResponseEntity.ok(bookingService.getUserBookings(userId));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }
}