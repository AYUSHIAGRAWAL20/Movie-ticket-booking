package com.moviebooking.dto.response;

import com.moviebooking.enums.SeatCategory;
import com.moviebooking.enums.SeatStatus;

import java.math.BigDecimal;

public class SeatResponse {

    private Long id;
    private String seatNumber;
    private String rowNumber;
    private SeatCategory category;
    private SeatStatus status;
    private BigDecimal price;

    public SeatResponse() {
    }

    public SeatResponse(Long id, String seatNumber, String rowNumber,
                        SeatCategory category, SeatStatus status, BigDecimal price) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.rowNumber = rowNumber;
        this.category = category;
        this.status = status;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(String rowNumber) {
        this.rowNumber = rowNumber;
    }

    public SeatCategory getCategory() {
        return category;
    }

    public void setCategory(SeatCategory category) {
        this.category = category;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "SeatResponse{" +
                "id=" + id +
                ", seatNumber='" + seatNumber + '\'' +
                ", rowNumber='" + rowNumber + '\'' +
                ", category=" + category +
                ", status=" + status +
                ", price=" + price +
                '}';
    }
}
