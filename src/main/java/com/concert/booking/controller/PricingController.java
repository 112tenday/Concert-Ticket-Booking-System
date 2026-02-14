package com.concert.booking.controller;

import com.concert.booking.dto.response.PricingAvailabilityResponse;
import com.concert.booking.service.ConcertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/concerts")
@RequiredArgsConstructor
@Tag(name = "3. Pricing & Availability")
public class PricingController {

    private final ConcertService concertService;

    @Operation(tags = "3. Pricing & Availability")
    @GetMapping("/{id}/pricing")
    public ResponseEntity<List<PricingAvailabilityResponse>> getPricing(@PathVariable Long id) {
        return ResponseEntity.ok(concertService.getConcertPricingAndAvailability(id));
    }

    @Operation(tags = "3. Pricing & Availability")
    @GetMapping("/{id}/availability")
    public ResponseEntity<List<PricingAvailabilityResponse>> getAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(concertService.getConcertPricingAndAvailability(id));
    }
}