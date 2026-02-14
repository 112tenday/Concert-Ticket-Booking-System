package com.concert.booking.service;

import com.concert.booking.dto.response.SettlementResponse;
import com.concert.booking.entity.Booking;
import com.concert.booking.entity.Concert;
import com.concert.booking.entity.TicketCategory;
import com.concert.booking.model.BookingStatus;
import com.concert.booking.repository.BookingRepository;
import com.concert.booking.repository.ConcertRepository;
import com.concert.booking.service.impl.SettlementServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SettlementServiceTest {

    @Mock
    private ConcertRepository concertRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private SettlementServiceImpl settlementService;

    @Test
    @DisplayName("pass count settlement report base on concert ID")
    void shouldGenerateSettlementReportSuccessfully() {
        Long concertId = 1L;
        Concert mockConcert = new Concert();
        mockConcert.setId(concertId);
        mockConcert.setName("Rock Anthem 2026");

        TicketCategory cat = new TicketCategory();
        cat.setTotalAllocation(100);
        mockConcert.setTicketCategories(List.of(cat));

        Booking b1 = new Booking();
        b1.setId(101L);
        b1.setStatus(BookingStatus.PENDING);
        b1.setTotalAmount(new BigDecimal("500000"));

        Booking b2 = new Booking();
        b2.setId(102L);
        b2.setStatus(BookingStatus.CANCELLED);
        b2.setTotalAmount(new BigDecimal("500000"));

        when(concertRepository.findById(concertId)).thenReturn(Optional.of(mockConcert));
        when(bookingRepository.findByConcertId(concertId)).thenReturn(List.of(b1, b2));

        SettlementResponse response = settlementService.getSettlementReport(concertId);

        assertNotNull(response);
        assertEquals("Rock Anthem 2026", response.getConcertName());
        assertEquals(new BigDecimal("500000"), response.getTotalRevenue());
        assertEquals(1, response.getTotalTicketsSold());
        assertEquals(1.0, response.getOccupancyRate());
    }
}