package com.moviebooking.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ShowResponse {

    private Long id;
    private MovieDetailsResponse movieDetails;
    private TheaterDetailsResponse theaterDetails;
    private LocalDateTime showTime;
    private BigDecimal basePriceRegular;
    private BigDecimal basePricePremium;
    private BigDecimal weekendMultiplier;

    public ShowResponse() {
    }

    public ShowResponse(Long id, MovieDetailsResponse movieDetails,
                        TheaterDetailsResponse theaterDetails, LocalDateTime showTime,
                        BigDecimal basePriceRegular, BigDecimal basePricePremium,
                        BigDecimal weekendMultiplier) {
        this.id = id;
        this.movieDetails = movieDetails;
        this.theaterDetails = theaterDetails;
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

    public MovieDetailsResponse getMovieDetails() {
        return movieDetails;
    }

    public void setMovieDetails(MovieDetailsResponse movieDetails) {
        this.movieDetails = movieDetails;
    }

    public TheaterDetailsResponse getTheaterDetails() {
        return theaterDetails;
    }

    public void setTheaterDetails(TheaterDetailsResponse theaterDetails) {
        this.theaterDetails = theaterDetails;
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
        return "ShowResponse{" +
                "id=" + id +
                ", movieDetails=" + movieDetails +
                ", theaterDetails=" + theaterDetails +
                ", showTime=" + showTime +
                ", basePriceRegular=" + basePriceRegular +
                ", basePricePremium=" + basePricePremium +
                ", weekendMultiplier=" + weekendMultiplier +
                '}';
    }

    public static class MovieDetailsResponse {
        private Long movieId;
        private String title;
        private Integer durationMinutes;

        public MovieDetailsResponse() {
        }

        public MovieDetailsResponse(Long movieId, String title, Integer durationMinutes) {
            this.movieId = movieId;
            this.title = title;
            this.durationMinutes = durationMinutes;
        }

        public Long getMovieId() {
            return movieId;
        }

        public void setMovieId(Long movieId) {
            this.movieId = movieId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getDurationMinutes() {
            return durationMinutes;
        }

        public void setDurationMinutes(Integer durationMinutes) {
            this.durationMinutes = durationMinutes;
        }

        @Override
        public String toString() {
            return "MovieDetailsResponse{" +
                    "movieId=" + movieId +
                    ", title='" + title + '\'' +
                    ", durationMinutes=" + durationMinutes +
                    '}';
        }
    }

    public static class TheaterDetailsResponse {
        private Long theaterId;
        private String name;
        private String address;

        public TheaterDetailsResponse() {
        }

        public TheaterDetailsResponse(Long theaterId, String name, String address) {
            this.theaterId = theaterId;
            this.name = name;
            this.address = address;
        }

        public Long getTheaterId() {
            return theaterId;
        }

        public void setTheaterId(Long theaterId) {
            this.theaterId = theaterId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        @Override
        public String toString() {
            return "TheaterDetailsResponse{" +
                    "theaterId=" + theaterId +
                    ", name='" + name + '\'' +
                    ", address='" + address + '\'' +
                    '}';
        }
    }
}
