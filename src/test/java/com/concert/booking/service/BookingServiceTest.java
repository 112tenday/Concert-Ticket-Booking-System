package com.concert.booking.service;

import com.concert.booking.dto.request.BookingRequest;
import com.concert.booking.dto.response.BookingResponse;
import com.concert.booking.entity.TicketCategory;
import com.concert.booking.repository.BookingRepository;
import com.concert.booking.repository.TicketCategoryRepository;
import com.concert.booking.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private TicketCategoryRepository ticketCategoryRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    @DisplayName("Must faill if Key duplicate")
    void shouldThrowExceptionWhenIdempotencyKeyExists() {
        BookingRequest request = new BookingRequest();
        request.setIdempotencyKey("DUPLICATE_KEY");

        when(bookingRepository.existsByIdempotencyKey("DUPLICATE_KEY")).thenReturn(true);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookingService.createBooking(request);
        });

        assertEquals("Duplicate request detected. A booking with this Idempotency Key already exists.", exception.getMessage());
        verify(bookingRepository, times(1)).existsByIdempotencyKey(anyString());
    }

    @Test
    @DisplayName("must faill if ticket Sold Out")
    void shouldThrowExceptionWhenSoldOut() {

        BookingRequest request = new BookingRequest();
        request.setTicketCategoryId(1L);
        request.setIdempotencyKey("UNIQUE_KEY");

        TicketCategory mockCategory = new TicketCategory();
        mockCategory.setAvailableStock(0);

        when(bookingRepository.existsByIdempotencyKey(anyString())).thenReturn(false);
        when(ticketCategoryRepository.findByIdWithLock(1L)).thenReturn(Optional.of(mockCategory));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookingService.createBooking(request);
        });

        assertTrue(exception.getMessage().contains("sold out"));
    }

    @Test
    @DisplayName("must pass Booking if data valid")
    void shouldSuccessfullyCreateBooking() {
        BookingRequest request = new BookingRequest();
        request.setTicketCategoryId(1L);
        request.setUserId(100L);
        request.setIdempotencyKey("NEW_UNIQUE_KEY");

        TicketCategory mockCategory = new TicketCategory();
        mockCategory.setId(1L);
        mockCategory.setBasePrice(new BigDecimal("1000000"));
        mockCategory.setTotalAllocation(100);
        mockCategory.setAvailableStock(50);

        when(bookingRepository.existsByIdempotencyKey(anyString())).thenReturn(false);
        when(ticketCategoryRepository.findByIdWithLock(1L)).thenReturn(Optional.of(mockCategory));

        when(bookingRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        BookingResponse response = bookingService.createBooking(request);

        assertNotNull(response);
        assertEquals("PENDING", response.getStatus().name());

        verify(bookingRepository, times(1)).save(any());
        assertEquals(49, mockCategory.getAvailableStock());
    }
}