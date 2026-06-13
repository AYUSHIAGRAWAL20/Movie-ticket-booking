package com.moviebooking.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreateSeatLayoutRequest {

    @NotNull(message = "Number of rows is required")
    @Min(value = 1, message = "Rows must be at least 1")
    private Integer rows;

    @NotNull(message = "Number of seats per row is required")
    @Min(value = 1, message = "Seats per row must be at least 1")
    private Integer seatsPerRow;

    private String premiumRows;

    public CreateSeatLayoutRequest() {
    }

    public CreateSeatLayoutRequest(Integer rows, Integer seatsPerRow, String premiumRows) {
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;
        this.premiumRows = premiumRows;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getSeatsPerRow() {
        return seatsPerRow;
    }

    public void setSeatsPerRow(Integer seatsPerRow) {
        this.seatsPerRow = seatsPerRow;
    }

    public String getPremiumRows() {
        return premiumRows;
    }

    public void setPremiumRows(String premiumRows) {
        this.premiumRows = premiumRows;
    }
}
