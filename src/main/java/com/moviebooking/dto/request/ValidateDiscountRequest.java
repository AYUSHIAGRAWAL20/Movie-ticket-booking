package com.moviebooking.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ValidateDiscountRequest {

    @NotBlank(message = "Discount code is required")
    private String code;

    public ValidateDiscountRequest() {
    }

    public ValidateDiscountRequest(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
