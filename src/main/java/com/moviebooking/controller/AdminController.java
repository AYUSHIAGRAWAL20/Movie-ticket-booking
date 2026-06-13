package com.moviebooking.controller;

import com.moviebooking.dto.request.*;
import com.moviebooking.entity.*;
import com.moviebooking.service.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final CityService cityService;
    private final TheaterService theaterService;
    private final MovieService movieService;
    private final ShowService showService;
    private final DiscountService discountService;

    public AdminController(CityService cityService,
                          TheaterService theaterService,
                          MovieService movieService,
                          ShowService showService,
                          DiscountService discountService) {
        this.cityService = cityService;
        this.theaterService = theaterService;
        this.movieService = movieService;
        this.showService = showService;
        this.discountService = discountService;
    }

    // ========== Cities ==========

    @PostMapping("/cities")
    public ResponseEntity<City> createCity(@Valid @RequestBody CreateCityRequest request) {
        City city = cityService.createCity(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(city);
    }

    @GetMapping("/cities")
    public ResponseEntity<List<City>> getAllCities() {
        List<City> cities = cityService.getAllCities();
        return ResponseEntity.ok(cities);
    }

    // ========== Theaters ==========

    @PostMapping("/theaters")
    public ResponseEntity<Theater> createTheater(@Valid @RequestBody CreateTheaterRequest request) {
        Theater theater = theaterService.createTheater(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(theater);
    }

    @GetMapping("/theaters")
    public ResponseEntity<List<Theater>> getTheaters(@RequestParam(required = false) Long cityId) {
        List<Theater> theaters;
        if (cityId != null) {
            theaters = theaterService.getTheatersByCityId(cityId);
        } else {
            theaters = theaterService.getAllTheaters();
        }
        return ResponseEntity.ok(theaters);
    }

    @PostMapping("/theaters/{id}/seat-layout")
    public ResponseEntity<SeatLayout> createSeatLayout(@PathVariable Long id,
                                                       @Valid @RequestBody CreateSeatLayoutRequest request) {
        SeatLayout seatLayout = theaterService.createSeatLayout(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(seatLayout);
    }

    // ========== Movies ==========

    @PostMapping("/movies")
    public ResponseEntity<Movie> createMovie(@Valid @RequestBody CreateMovieRequest request) {
        Movie movie = movieService.createMovie(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(movie);
    }

    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        return ResponseEntity.ok(movies);
    }

    // ========== Shows ==========

    @PostMapping("/shows")
    public ResponseEntity<Show> createShow(@Valid @RequestBody CreateShowRequest request) {
        Show show = showService.createShow(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(show);
    }

    @PutMapping("/shows/{id}/pricing")
    public ResponseEntity<Show> updatePricing(@PathVariable Long id,
                                              @Valid @RequestBody UpdateShowPricingRequest request) {
        Show show = showService.updateShowPricing(id, request);
        return ResponseEntity.ok(show);
    }

    // ========== Discounts ==========

    @PostMapping("/discounts")
    public ResponseEntity<DiscountCode> createDiscount(@Valid @RequestBody CreateDiscountCodeRequest request) {
        DiscountCode discountCode = discountService.createDiscountCode(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(discountCode);
    }

    @GetMapping("/discounts")
    public ResponseEntity<List<DiscountCode>> getAllDiscounts() {
        List<DiscountCode> discounts = discountService.getAllDiscountCodes();
        return ResponseEntity.ok(discounts);
    }

    @DeleteMapping("/discounts/{id}")
    public ResponseEntity<Void> deactivateDiscount(@PathVariable Long id) {
        discountService.deactivateDiscountCode(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
