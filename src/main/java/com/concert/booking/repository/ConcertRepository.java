package com.concert.booking.repository;

import com.concert.booking.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Long> {

    @Query("SELECT c FROM Concert c WHERE " +
            "(:name IS NULL OR LOWER(CAST(c.name AS text)) LIKE LOWER(CONCAT('%', CAST(:name AS text), '%'))) AND " +
            "(:artist IS NULL OR LOWER(CAST(c.artistName AS text)) LIKE LOWER(CONCAT('%', CAST(:artist AS text), '%'))) AND " +
            "(:venue IS NULL OR LOWER(CAST(c.venue AS text)) LIKE LOWER(CONCAT('%', CAST(:venue AS text), '%')))")
    List<Concert> findWithFilters(
            @Param("name") String name,
            @Param("artist") String artist,
            @Param("venue") String venue);
}