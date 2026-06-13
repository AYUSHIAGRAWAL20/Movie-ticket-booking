package com.moviebooking.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class HoldSeatsRequest {

    @NotNull(message = "Show ID is required")
    private Long showId;

    @NotEmpty(message = "Seat IDs list cannot be empty")
    private List<Long> seatIds;

    private String discountCode;

    public HoldSeatsRequest() {
    }

    public HoldSeatsRequest(Long showId, List<Long> seatIds, String discountCode) {
        this.showId = showId;
        this.seatIds = seatIds;
        this.discountCode = discountCode;
    }

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public List<Long> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(List<Long> seatIds) {
        this.seatIds = seatIds;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }
}
