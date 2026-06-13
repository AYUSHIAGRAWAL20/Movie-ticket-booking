package com.moviebooking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "seat_layouts")
public class SeatLayout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "theater_id", nullable = false, unique = true)
    private Theater theater;

    @Min(value = 1, message = "Rows must be at least 1")
    @Column(nullable = false)
    private Integer rows;

    @Min(value = 1, message = "Seats per row must be at least 1")
    @Column(nullable = false)
    private Integer seatsPerRow;

    @Column(length = 500)
    private String premiumRows;

    public SeatLayout() {
    }

    public SeatLayout(Theater theater, Integer rows, Integer seatsPerRow, String premiumRows) {
        this.theater = theater;
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;
        this.premiumRows = premiumRows;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Theater getTheater() {
        return theater;
    }

    public void setTheater(Theater theater) {
        this.theater = theater;
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

    @Override
    public String toString() {
        return "SeatLayout{" +
                "id=" + id +
                ", theater=" + theater +
                ", rows=" + rows +
                ", seatsPerRow=" + seatsPerRow +
                ", premiumRows='" + premiumRows + '\'' +
                '}';
    }
}
