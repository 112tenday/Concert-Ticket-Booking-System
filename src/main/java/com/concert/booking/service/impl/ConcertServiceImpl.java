package com.concert.booking.service.impl;

import com.concert.booking.dto.request.ConcertRequest;
import com.concert.booking.entity.Concert;
import com.concert.booking.entity.TicketCategory;
import com.concert.booking.model.ConcertStatus;
import com.concert.booking.repository.ConcertRepository;
import com.concert.booking.repository.TicketCategoryRepository;
import com.concert.booking.service.ConcertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertServiceImpl implements ConcertService {

    private final ConcertRepository concertRepository;
    private final TicketCategoryRepository ticketCategoryRepository;

    @Override
    @Transactional
    public Concert createConcert(ConcertRequest request) {
        Concert concert = new Concert();
        concert.setName(request.getName());
        concert.setArtistName(request.getArtistName());
        concert.setVenue(request.getVenue());
        concert.setConcertDate(request.getConcertDate());
        concert.setStatus(ConcertStatus.UPCOMING);

        Concert savedConcert = concertRepository.save(concert);

        TicketCategory category = new TicketCategory();
        category.setConcert(savedConcert);
        category.setName("General Admission");
        category.setBasePrice(request.getBasePrice());
        category.setTotalAllocation(request.getTotalCapacity());
        category.setAvailableStock(request.getTotalCapacity());

        ticketCategoryRepository.save(category);

        return savedConcert;
    }

    @Override
    public List<Concert> getAllConcerts() {
        return concertRepository.findAll();
    }

    @Override
    public Concert getConcertById(Long id) {
        return concertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Concert not found with id: " + id));
    }

    @Override
    @Transactional
    public Concert updateConcert(Long id, ConcertRequest request) {
        Concert concert = getConcertById(id);
        concert.setName(request.getName());
        concert.setArtistName(request.getArtistName());
        concert.setVenue(request.getVenue());
        concert.setConcertDate(request.getConcertDate());
        return concertRepository.save(concert);
    }

    @Override
    public List<Concert> searchConcerts(String name, String artist, String venue) {
        return concertRepository.findWithFilters(name, artist, venue);
    }

    @Override
    public List<com.concert.booking.dto.response.PricingAvailabilityResponse> getConcertPricingAndAvailability(Long concertId) {
        Concert concert = getConcertById(concertId);

        return concert.getTicketCategories().stream().map(category -> {
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

            java.math.BigDecimal multiplier = java.math.BigDecimal.valueOf(1 + demandMultiplier);
            java.math.BigDecimal currentPrice = category.getBasePrice().multiply(multiplier);

            return com.concert.booking.dto.response.PricingAvailabilityResponse.builder()
                    .categoryId(category.getId())
                    .categoryName(category.getName())
                    .basePrice(category.getBasePrice())
                    .currentPrice(currentPrice)
                    .availableStock(category.getAvailableStock())
                    .totalCapacity(category.getTotalAllocation())
                    .build();
        }).toList();
    }
    @Override
    public List<Concert> getAllConcerts(String name, String artist, String venue) {
        return concertRepository.findWithFilters(name, artist, venue);
    }
}