package com.moviebooking.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class HoldResponse {

    private Long holdId;
    private LocalDateTime expiryTime;
    private BigDecimal totalAmount;
    private List<SeatResponse> seats;

    public HoldResponse() {
    }

    public HoldResponse(Long holdId, LocalDateTime expiryTime,
                        BigDecimal totalAmount, List<SeatResponse> seats) {
        this.holdId = holdId;
        this.expiryTime = expiryTime;
        this.totalAmount = totalAmount;
        this.seats = seats;
    }

    public Long getHoldId() {
        return holdId;
    }

    public void setHoldId(Long holdId) {
        this.holdId = holdId;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<SeatResponse> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatResponse> seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {
        return "HoldResponse{" +
                "holdId=" + holdId +
                ", expiryTime=" + expiryTime +
                ", totalAmount=" + totalAmount +
                ", seats=" + seats +
                '}';
    }
}
