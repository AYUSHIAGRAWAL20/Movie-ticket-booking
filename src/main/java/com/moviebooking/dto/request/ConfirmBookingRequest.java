package com.moviebooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ConfirmBookingRequest {

    @NotNull(message = "Hold ID is required")
    private Long holdId;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    public ConfirmBookingRequest() {
    }

    public ConfirmBookingRequest(Long holdId, String paymentMethod) {
        this.holdId = holdId;
        this.paymentMethod = paymentMethod;
    }

    public Long getHoldId() {
        return holdId;
    }

    public void setHoldId(Long holdId) {
        this.holdId = holdId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
