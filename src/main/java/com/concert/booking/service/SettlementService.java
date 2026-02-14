package com.concert.booking.service;

import com.concert.booking.dto.response.DashboardResponse;
import com.concert.booking.dto.response.SettlementResponse;
import com.concert.booking.dto.response.TransactionResponse;

import java.util.List;

public interface SettlementService {
    SettlementResponse getSettlementReport(Long concertId);

    DashboardResponse getDashboardAnalytics();

    List<TransactionResponse> getAllTransactions();
}