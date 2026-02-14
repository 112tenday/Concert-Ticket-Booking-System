package com.concert.booking.entity;

import com.concert.booking.model.ConcertStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "concerts")
public class Concert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "artist_name", nullable = false)
    private String artistName;

    @Column(nullable = false)
    private String venue;

    @Column(name = "concert_date", nullable = false)
    private LocalDateTime concertDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConcertStatus status;

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL)
    private List<TicketCategory> ticketCategories;
}