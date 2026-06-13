package com.moviebooking.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreateShowRequest {

    @NotNull(message = "Movie ID is required")
    private Long movieId;

    @NotNull(message = "Theater ID is required")
    private Long theaterId;

    @NotNull(message = "Show time is required")
    @Future(message = "Show time must be in the future")
    private LocalDateTime showTime;

    @NotNull(message = "Base price for regular seats is required")
    @Positive(message = "Base price for regular seats must be positive")
    private BigDecimal basePriceRegular;

    @NotNull(message = "Base price for premium seats is required")
    @Positive(message = "Base price for premium seats must be positive")
    private BigDecimal basePricePremium;

    @Positive(message = "Weekend multiplier must be positive if provided")
    private BigDecimal weekendMultiplier;

    public CreateShowRequest() {
    }

    public CreateShowRequest(Long movieId, Long theaterId, LocalDateTime showTime,
                           BigDecimal basePriceRegular, BigDecimal basePricePremium,
                           BigDecimal weekendMultiplier) {
        this.movieId = movieId;
        this.theaterId = theaterId;
        this.showTime = showTime;
        this.basePriceRegular = basePriceRegular;
        this.basePricePremium = basePricePremium;
        this.weekendMultiplier = weekendMultiplier;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Long getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(Long theaterId) {
        this.theaterId = theaterId;
    }

    public LocalDateTime getShowTime() {
        return showTime;
    }

    public void setShowTime(LocalDateTime showTime) {
        this.showTime = showTime;
    }

    public BigDecimal getBasePriceRegular() {
        return basePriceRegular;
    }

    public void setBasePriceRegular(BigDecimal basePriceRegular) {
        this.basePriceRegular = basePriceRegular;
    }

    public BigDecimal getBasePricePremium() {
        return basePricePremium;
    }

    public void setBasePricePremium(BigDecimal basePricePremium) {
        this.basePricePremium = basePricePremium;
    }

    public BigDecimal getWeekendMultiplier() {
        return weekendMultiplier;
    }

    public void setWeekendMultiplier(BigDecimal weekendMultiplier) {
        this.weekendMultiplier = weekendMultiplier;
    }
}
