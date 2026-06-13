package com.moviebooking.repository;

import com.moviebooking.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
    @Query("SELECT s FROM Show s WHERE " +
            "(:cityId IS NULL OR s.theater.city.id = :cityId) AND " +
            "(:movieId IS NULL OR s.movie.id = :movieId) AND " +
            "(:startDate IS NULL OR s.showTime >= :startDate) AND " +
            "(:endDate IS NULL OR s.showTime <= :endDate)")
    List<Show> findByFilters(
            @Param("cityId") Long cityId,
            @Param("movieId") Long movieId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
