package com.moviebooking.repository;

import com.moviebooking.entity.SeatLayout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatLayoutRepository extends JpaRepository<SeatLayout, Long> {
    Optional<SeatLayout> findByTheaterId(Long theaterId);
}
