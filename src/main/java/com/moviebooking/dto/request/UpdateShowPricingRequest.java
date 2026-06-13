package com.moviebooking.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class UpdateShowPricingRequest {

    @NotNull(message = "Base price for regular seats is required")
    @Positive(message = "Base price for regular seats must be positive")
    private BigDecimal basePriceRegular;

    @NotNull(message = "Base price for premium seats is required")
    @Positive(message = "Base price for premium seats must be positive")
    private BigDecimal basePricePremium;

    @Positive(message = "Weekend multiplier must be positive if provided")
    private BigDecimal weekendMultiplier;

    public UpdateShowPricingRequest() {
    }

    public UpdateShowPricingRequest(BigDecimal basePriceRegular, BigDecimal basePricePremium, BigDecimal weekendMultiplier) {
        this.basePriceRegular = basePriceRegular;
        this.basePricePremium = basePricePremium;
        this.weekendMultiplier = weekendMultiplier;
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
