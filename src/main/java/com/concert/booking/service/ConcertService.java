package com.concert.booking.service;

import com.concert.booking.dto.request.ConcertRequest;
import com.concert.booking.dto.response.PricingAvailabilityResponse;
import com.concert.booking.entity.Concert;
import java.util.List;

public interface ConcertService {
    Concert createConcert(ConcertRequest request);

    List<Concert> getAllConcerts();
    Concert getConcertById(Long id);
    Concert updateConcert(Long id, ConcertRequest request);
    List<Concert> searchConcerts(String name, String artist, String venue);
    List<Concert> getAllConcerts(String name, String artist, String venue);

    List<PricingAvailabilityResponse> getConcertPricingAndAvailability
            (Long concertId);
}