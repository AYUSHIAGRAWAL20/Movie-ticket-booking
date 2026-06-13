package com.moviebooking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "shows")
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    @NotNull(message = "Show time cannot be null")
    @Column(nullable = false)
    private LocalDateTime showTime;

    @NotNull(message = "Base price for regular seats cannot be null")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal basePriceRegular;

    @NotNull(message = "Base price for premium seats cannot be null")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal basePricePremium;

    @NotNull(message = "Weekend multiplier cannot be null")
    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal weekendMultiplier = new BigDecimal("1.00");

    public Show() {
    }

    public Show(Movie movie, Theater theater, LocalDateTime showTime,
                BigDecimal basePriceRegular, BigDecimal basePricePremium,
                BigDecimal weekendMultiplier) {
        this.movie = movie;
        this.theater = theater;
        this.showTime = showTime;
        this.basePriceRegular = basePriceRegular;
        this.basePricePremium = basePricePremium;
        this.weekendMultiplier = weekendMultiplier;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Theater getTheater() {
        return theater;
    }

    public void setTheater(Theater theater) {
        this.theater = theater;
    }

    public LocalDateTime getShowTime() {
        return showTime;
    }

    public void setShowTime(LocalDateTime showTime) {
        this.showTime = showTime;
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

    @Override
    public String toString() {
        return "Show{" +
                "id=" + id +
                ", movie=" + movie +
                ", theater=" + theater +
                ", showTime=" + showTime +
                ", basePriceRegular=" + basePriceRegular +
                ", basePricePremium=" + basePricePremium +
                ", weekendMultiplier=" + weekendMultiplier +
                '}';
    }
}
