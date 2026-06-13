package com.moviebooking.controller;

import com.moviebooking.dto.request.ConfirmBookingRequest;
import com.moviebooking.dto.request.HoldSeatsRequest;
import com.moviebooking.dto.request.ValidateDiscountRequest;
import com.moviebooking.dto.response.BookingResponse;
import com.moviebooking.dto.response.CancelBookingResponse;
import com.moviebooking.dto.response.DiscountValidationResponse;
import com.moviebooking.dto.response.HoldResponse;
import com.moviebooking.entity.Booking;
import com.moviebooking.entity.Seat;
import com.moviebooking.entity.Show;
import com.moviebooking.service.BookingService;
import com.moviebooking.service.DiscountService;
import com.moviebooking.service.ShowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BookingController {

    private final ShowService showService;
    private final BookingService bookingService;
    private final DiscountService discountService;

    public BookingController(ShowService showService, BookingService bookingService, DiscountService discountService) {
        this.showService = showService;
        this.bookingService = bookingService;
        this.discountService = discountService;
    }

    // Browse Shows
    @GetMapping("/shows")
    public ResponseEntity<List<Show>> browseShows(
            @RequestParam(required = false) Long cityId,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) Long movieId) {

        LocalDate showDate = date != null ? LocalDate.parse(date) : null;
        List<Show> shows = showService.getShowsByFilters(cityId, movieId, showDate);
        return ResponseEntity.ok(shows);
    }

    @GetMapping("/shows/{id}/seats")
    public ResponseEntity<List<Seat>> getSeatsForShow(@PathVariable Long id) {
        List<Seat> seats = showService.getSeatsForShow(id);
        return ResponseEntity.ok(seats);
    }

    // Booking Operations
    @PostMapping("/bookings/hold")
    public ResponseEntity<HoldResponse> holdSeats(
            Principal principal,
            @Valid @RequestBody HoldSeatsRequest request) {

        String username = principal.getName();
        HoldResponse response = bookingService.holdSeats(username, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/bookings/confirm")
    public ResponseEntity<BookingResponse> confirmBooking(
            Principal principal,
            @Valid @RequestBody ConfirmBookingRequest request) {

        String username = principal.getName();
        BookingResponse response = bookingService.confirmBooking(username, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/bookings/hold/{id}")
    public ResponseEntity<Void> releaseHold(
            Principal principal,
            @PathVariable Long id) {

        String username = principal.getName();
        bookingService.releaseHoldManually(username, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Booking History
    @GetMapping("/bookings/my-bookings")
    public ResponseEntity<List<Booking>> getMyBookings(Principal principal) {
        String username = principal.getName();
        List<Booking> bookings = bookingService.getMyBookings(username);
        return ResponseEntity.ok(bookings);
    }

    // Cancellation
    @PostMapping("/bookings/{id}/cancel")
    public ResponseEntity<CancelBookingResponse> cancelBooking(
            Principal principal,
            @PathVariable Long id) {

        String username = principal.getName();
        CancelBookingResponse response = bookingService.cancelBooking(username, id);
        return ResponseEntity.ok(response);
    }

    // Discount Validation
    @PostMapping("/discounts/validate")
    public ResponseEntity<DiscountValidationResponse> validateDiscount(
            @Valid @RequestBody ValidateDiscountRequest request) {

        DiscountValidationResponse response = discountService.validateDiscountCode(request.getCode());
        return ResponseEntity.ok(response);
    }
}
