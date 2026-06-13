package com.moviebooking.service;

import com.moviebooking.dto.request.CreateShowRequest;
import com.moviebooking.dto.request.UpdateShowPricingRequest;
import com.moviebooking.entity.*;
import com.moviebooking.enums.SeatCategory;
import com.moviebooking.enums.SeatStatus;
import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ShowService {

    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final TheaterRepository theaterRepository;
    private final SeatLayoutRepository seatLayoutRepository;
    private final SeatRepository seatRepository;

    public ShowService(ShowRepository showRepository,
                      MovieRepository movieRepository,
                      TheaterRepository theaterRepository,
                      SeatLayoutRepository seatLayoutRepository,
                      SeatRepository seatRepository) {
        this.showRepository = showRepository;
        this.movieRepository = movieRepository;
        this.theaterRepository = theaterRepository;
        this.seatLayoutRepository = seatLayoutRepository;
        this.seatRepository = seatRepository;
    }

    @Transactional
    public Show createShow(CreateShowRequest request) {
        // Validate movie exists
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Movie not found with id: " + request.getMovieId()));

        // Validate theater exists
        Theater theater = theaterRepository.findById(request.getTheaterId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Theater not found with id: " + request.getTheaterId()));

        // Get seat layout for the theater
        SeatLayout seatLayout = seatLayoutRepository.findByTheaterId(theater.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Seat layout not found for theater id: " + theater.getId()));

        // Create show entity with default weekendMultiplier if not provided
        BigDecimal weekendMultiplier = request.getWeekendMultiplier();
        if (weekendMultiplier == null) {
            weekendMultiplier = new BigDecimal("1.00");
        }

        Show show = new Show(
                movie,
                theater,
                request.getShowTime(),
                request.getBasePriceRegular(),
                request.getBasePricePremium(),
                weekendMultiplier
        );

        // Save show first to get the ID
        show = showRepository.save(show);

        // Generate seats from layout
        List<Seat> seats = generateSeatsFromLayout(show, seatLayout);

        // Save all seats
        seatRepository.saveAll(seats);

        return show;
    }

    private List<Seat> generateSeatsFromLayout(Show show, SeatLayout layout) {
        List<Seat> seats = new ArrayList<>();

        // Parse premium rows into a Set for quick lookup
        Set<Integer> premiumRowsSet = parsePremiumRows(layout.getPremiumRows());

        // Generate seats for each row
        for (int rowIndex = 0; rowIndex < layout.getRows(); rowIndex++) {
            String rowLetter = String.valueOf((char) ('A' + rowIndex));

            // Determine if this row is premium (1-based row number)
            int rowNumber = rowIndex + 1;
            boolean isPremiumRow = premiumRowsSet.contains(rowNumber);
            SeatCategory category = isPremiumRow ? SeatCategory.PREMIUM : SeatCategory.REGULAR;

            // Generate seats for this row
            for (int seatIndex = 0; seatIndex < layout.getSeatsPerRow(); seatIndex++) {
                int seatNum = seatIndex + 1;
                String seatNumber = rowLetter + seatNum;

                Seat seat = new Seat(
                        show,
                        seatNumber,
                        rowLetter,
                        category,
                        SeatStatus.AVAILABLE
                );

                seats.add(seat);
            }
        }

        return seats;
    }

    private Set<Integer> parsePremiumRows(String premiumRows) {
        Set<Integer> premiumRowsSet = new HashSet<>();

        if (premiumRows == null || premiumRows.trim().isEmpty()) {
            return premiumRowsSet;
        }

        String[] rows = premiumRows.split(",");
        for (String row : rows) {
            try {
                premiumRowsSet.add(Integer.parseInt(row.trim()));
            } catch (NumberFormatException e) {
                // Skip invalid row numbers
            }
        }

        return premiumRowsSet;
    }

    public Show getShowById(Long id) {
        return showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Show not found with id: " + id));
    }

    public List<Show> getShowsByFilters(Long cityId, Long movieId, LocalDate date) {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        if (date != null) {
            startDate = date.atStartOfDay();
            endDate = date.atTime(LocalTime.MAX);
        }

        return showRepository.findByFilters(cityId, movieId, startDate, endDate);
    }

    @Transactional
    public Show updateShowPricing(Long showId, UpdateShowPricingRequest request) {
        Show show = getShowById(showId);

        show.setBasePriceRegular(request.getBasePriceRegular());
        show.setBasePricePremium(request.getBasePricePremium());

        if (request.getWeekendMultiplier() != null) {
            show.setWeekendMultiplier(request.getWeekendMultiplier());
        }

        return showRepository.save(show);
    }

    public List<Seat> getSeatsForShow(Long showId) {
        // Verify show exists
        getShowById(showId);

        return seatRepository.findByShowId(showId);
    }
}
