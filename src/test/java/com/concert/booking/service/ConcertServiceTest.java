package com.concert.booking.service;

import com.concert.booking.entity.Concert;
import com.concert.booking.repository.ConcertRepository;
import com.concert.booking.service.impl.ConcertServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConcertServiceTest {

    @Mock
    private ConcertRepository concertRepository;

    @InjectMocks
    private ConcertServiceImpl concertService;

    @Test
    @DisplayName("return concert ID")
    void shouldReturnConcertWhenIdExists() {
        Concert concert = new Concert();
        concert.setId(1L);
        concert.setName("Taylor Swift Eras Tour");

        when(concertRepository.findById(1L)).thenReturn(Optional.of(concert));

        Concert result = concertService.getConcertById(1L);
        assertEquals("Taylor Swift Eras Tour", result.getName());
    }

    @Test
    @DisplayName("retutn empty list of nothing concert")
    void shouldReturnEmptyListWhenNoConcertFound() {
        when(concertRepository.findWithFilters(any(), any(), any())).thenReturn(List.of());

        List<Concert> result = concertService.getAllConcerts(null, null, null);
        assertTrue(result.isEmpty());
    }
}