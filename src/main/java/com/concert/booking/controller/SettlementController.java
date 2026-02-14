package com.concert.booking.controller;

import com.concert.booking.dto.response.DashboardResponse;
import com.concert.booking.dto.response.SettlementResponse;
import com.concert.booking.dto.response.TransactionResponse;
import com.concert.booking.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "4. Settlement & Reports")
public class SettlementController {

    private final SettlementService settlementService;

    @GetMapping("/concerts/{id}/settlement")
    public ResponseEntity<SettlementResponse> getSettlementReport(@PathVariable Long id) {
        return ResponseEntity.ok(settlementService.getSettlementReport(id));
    }
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        return ResponseEntity.ok(settlementService.getAllTransactions());
    }
    @GetMapping("/analytics/dashboard")
    public ResponseEntity<DashboardResponse> getDashboard() {
        return ResponseEntity.ok(settlementService.getDashboardAnalytics());
    }
}