package com.moviebooking.repository;

import com.moviebooking.entity.SeatHold;
import com.moviebooking.enums.HoldStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SeatHoldRepository extends JpaRepository<SeatHold, Long> {
    List<SeatHold> findByStatusAndExpiryTimeBefore(HoldStatus status, LocalDateTime expiryTime);
}
