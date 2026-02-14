package com.concert.booking.service;

import com.concert.booking.dto.request.BookingRequest;
import com.concert.booking.dto.response.BookingResponse;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingRequest request);
    BookingResponse getBookingDetails(Long id);
    List<BookingResponse> getUserBookings(Long userId);
    void cancelBooking(Long id);
}