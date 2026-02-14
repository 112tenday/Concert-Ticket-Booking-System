package com.concert.booking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "ticket_categories")
public class TicketCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "concert_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Concert concert;

    @Column(nullable = false)
    private String name;

    @Column(name = "base_price", nullable = false)
    private java.math.BigDecimal basePrice;

    @Column(name = "total_allocation", nullable = false)
    private Integer totalAllocation;

    @Column(name = "available_stock", nullable = false)
    private Integer availableStock;

    @Version
    private Long version;
}