package com.moviebooking.service;

import com.moviebooking.entity.Seat;
import com.moviebooking.entity.SeatHold;
import com.moviebooking.enums.HoldStatus;
import com.moviebooking.enums.SeatStatus;
import com.moviebooking.repository.SeatHoldRepository;
import com.moviebooking.repository.SeatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class SeatReleaseScheduler {
    private static final Logger logger = LoggerFactory.getLogger(SeatReleaseScheduler.class);

    private final SeatHoldRepository seatHoldRepository;
    private final SeatRepository seatRepository;

    public SeatReleaseScheduler(SeatHoldRepository seatHoldRepository, SeatRepository seatRepository) {
        this.seatHoldRepository = seatHoldRepository;
        this.seatRepository = seatRepository;
    }

    @Scheduled(fixedRate = 60000) // every 60 seconds
    @Transactional
    public void releaseExpiredHolds() {
        LocalDateTime now = LocalDateTime.now();
        List<SeatHold> expiredHolds = seatHoldRepository.findByStatusAndExpiryTimeBefore(
            HoldStatus.ACTIVE, now
        );

        for (SeatHold hold : expiredHolds) {
            // Get seats from the hold
            List<Seat> seats = hold.getSeats();

            // Update each seat to AVAILABLE
            for (Seat seat : seats) {
                seat.setStatus(SeatStatus.AVAILABLE);
                seatRepository.save(seat);
            }

            // Update hold status to EXPIRED
            hold.setStatus(HoldStatus.EXPIRED);
            seatHoldRepository.save(hold);
        }

        if (!expiredHolds.isEmpty()) {
            logger.info("Released {} expired seat holds", expiredHolds.size());
        }
    }
}
