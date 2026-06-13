package com.moviebooking.repository;

import com.moviebooking.entity.Seat;
import com.moviebooking.enums.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByShowId(Long showId);

    List<Seat> findByShowIdAndStatus(Long showId, SeatStatus status);

    List<Seat> findByIdIn(List<Long> ids);
}
