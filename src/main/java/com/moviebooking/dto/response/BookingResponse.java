package com.moviebooking.dto.response;

import com.moviebooking.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class BookingResponse {

    private Long id;
    private ShowResponse showDetails;
    private List<SeatResponse> seats;
    private BigDecimal totalAmount;
    private BigDecimal discountApplied;
    private String discountCode;
    private LocalDateTime bookingTime;
    private BookingStatus status;

    public BookingResponse() {
    }

    public BookingResponse(Long id, ShowResponse showDetails,
                           List<SeatResponse> seats, BigDecimal totalAmount,
                           BigDecimal discountApplied, String discountCode,
                           LocalDateTime bookingTime, BookingStatus status) {
        this.id = id;
        this.showDetails = showDetails;
        this.seats = seats;
        this.totalAmount = totalAmount;
        this.discountApplied = discountApplied;
        this.discountCode = discountCode;
        this.bookingTime = bookingTime;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShowResponse getShowDetails() {
        return showDetails;
    }

    public void setShowDetails(ShowResponse showDetails) {
        this.showDetails = showDetails;
    }

    public List<SeatResponse> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatResponse> seats) {
        this.seats = seats;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDiscountApplied() {
        return discountApplied;
    }

    public void setDiscountApplied(BigDecimal discountApplied) {
        this.discountApplied = discountApplied;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BookingResponse{" +
                "id=" + id +
                ", showDetails=" + showDetails +
                ", seats=" + seats +
                ", totalAmount=" + totalAmount +
                ", discountApplied=" + discountApplied +
                ", discountCode='" + discountCode + '\'' +
                ", bookingTime=" + bookingTime +
                ", status=" + status +
                '}';
    }
}
