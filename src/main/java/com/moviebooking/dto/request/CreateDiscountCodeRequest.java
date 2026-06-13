package com.moviebooking.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class CreateDiscountCodeRequest {

    @NotBlank(message = "Discount code is required")
    private String code;

    @NotNull(message = "Percentage off is required")
    @Min(value = 0, message = "Percentage off must be at least 0")
    @Max(value = 100, message = "Percentage off cannot exceed 100")
    private Integer percentageOff;

    @NotNull(message = "Valid from date is required")
    private LocalDateTime validFrom;

    @NotNull(message = "Valid until date is required")
    private LocalDateTime validUntil;

    public CreateDiscountCodeRequest() {
    }

    public CreateDiscountCodeRequest(String code, Integer percentageOff, LocalDateTime validFrom, LocalDateTime validUntil) {
        this.code = code;
        this.percentageOff = percentageOff;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getPercentageOff() {
        return percentageOff;
    }

    public void setPercentageOff(Integer percentageOff) {
        this.percentageOff = percentageOff;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }
}
