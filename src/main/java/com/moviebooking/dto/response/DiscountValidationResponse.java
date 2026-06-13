package com.moviebooking.dto.response;

import java.time.LocalDateTime;

public class DiscountValidationResponse {

    private Boolean valid;
    private Integer percentageOff;
    private LocalDateTime validUntil;

    public DiscountValidationResponse() {
    }

    public DiscountValidationResponse(Boolean valid, Integer percentageOff, LocalDateTime validUntil) {
        this.valid = valid;
        this.percentageOff = percentageOff;
        this.validUntil = validUntil;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Integer getPercentageOff() {
        return percentageOff;
    }

    public void setPercentageOff(Integer percentageOff) {
        this.percentageOff = percentageOff;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }

    @Override
    public String toString() {
        return "DiscountValidationResponse{" +
                "valid=" + valid +
                ", percentageOff=" + percentageOff +
                ", validUntil=" + validUntil +
                '}';
    }
}
