package com.moviebooking.dto.response;

import java.math.BigDecimal;

public class CancelBookingResponse {

    private Long bookingId;
    private BigDecimal refundAmount;
    private BigDecimal refundPercentage;
    private String message;

    public CancelBookingResponse() {
    }

    public CancelBookingResponse(Long bookingId, BigDecimal refundAmount,
                                 BigDecimal refundPercentage, String message) {
        this.bookingId = bookingId;
        this.refundAmount = refundAmount;
        this.refundPercentage = refundPercentage;
        this.message = message;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public BigDecimal getRefundPercentage() {
        return refundPercentage;
    }

    public void setRefundPercentage(BigDecimal refundPercentage) {
        this.refundPercentage = refundPercentage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CancelBookingResponse{" +
                "bookingId=" + bookingId +
                ", refundAmount=" + refundAmount +
                ", refundPercentage=" + refundPercentage +
                ", message='" + message + '\'' +
                '}';
    }
}
