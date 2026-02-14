package com.concert.booking.controller;

import com.concert.booking.dto.request.ConcertRequest;
import com.concert.booking.entity.Concert;
import com.concert.booking.service.ConcertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1/concerts")
@RequiredArgsConstructor
@Tag(name = "1. Concert Management")
public class ConcertController {

    private final ConcertService concertService;

    @PostMapping
    public ResponseEntity<Concert> createConcert(@Valid @RequestBody ConcertRequest request) {
        Concert newConcert = concertService.createConcert(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newConcert);
    }

    @GetMapping
    public ResponseEntity<List<Concert>> getAllConcerts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String artist,
            @RequestParam(required = false) String venue) {
        return ResponseEntity.ok(concertService.getAllConcerts(name, artist, venue));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Concert> getConcert(@PathVariable Long id) {
        return ResponseEntity.ok(concertService.getConcertById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Concert> updateConcert(
            @PathVariable Long id,
            @Valid @RequestBody ConcertRequest request) {
        return ResponseEntity.ok(concertService.updateConcert(id, request));
    }

}