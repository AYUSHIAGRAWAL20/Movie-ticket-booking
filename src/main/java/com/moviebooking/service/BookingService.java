package com.moviebooking.service;

import com.moviebooking.dto.request.ConfirmBookingRequest;
import com.moviebooking.dto.request.HoldSeatsRequest;
import com.moviebooking.dto.response.BookingResponse;
import com.moviebooking.dto.response.CancelBookingResponse;
import com.moviebooking.dto.response.HoldResponse;
import com.moviebooking.dto.response.SeatResponse;
import com.moviebooking.dto.response.ShowResponse;
import com.moviebooking.entity.*;
import com.moviebooking.enums.BookingStatus;
import com.moviebooking.enums.HoldStatus;
import com.moviebooking.enums.SeatCategory;
import com.moviebooking.enums.SeatStatus;
import com.moviebooking.exception.*;
import com.moviebooking.repository.BookingRepository;
import com.moviebooking.repository.SeatHoldRepository;
import com.moviebooking.repository.SeatRepository;
import com.moviebooking.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final SeatRepository seatRepository;
    private final ShowRepository showRepository;
    private final SeatHoldRepository seatHoldRepository;
    private final BookingRepository bookingRepository;
    private final DiscountService discountService;

    @Value("${seat.hold.timeout.minutes:10}")
    private int holdTimeoutMinutes;

    public BookingService(SeatRepository seatRepository,
                         ShowRepository showRepository,
                         SeatHoldRepository seatHoldRepository,
                         BookingRepository bookingRepository,
                         DiscountService discountService) {
        this.seatRepository = seatRepository;
        this.showRepository = showRepository;
        this.seatHoldRepository = seatHoldRepository;
        this.bookingRepository = bookingRepository;
        this.discountService = discountService;
    }

    @Transactional
    public HoldResponse holdSeats(String userId, HoldSeatsRequest request) {
        // Get show
        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Show not found with id: " + request.getShowId()));

        // Get all seats by IDs
        List<Seat> seats = seatRepository.findAllById(request.getSeatIds());

        // Validate all seats exist
        if (seats.size() != request.getSeatIds().size()) {
            throw new ResourceNotFoundException("One or more seats not found");
        }

        // Validate all seats belong to the requested show
        for (Seat seat : seats) {
            if (!seat.getShow().getId().equals(request.getShowId())) {
                throw new SeatNotAvailableException(
                        "Seat " + seat.getSeatNumber() + " does not belong to this show");
            }
        }

        // Validate all seats are AVAILABLE
        List<String> unavailableSeats = new ArrayList<>();
        for (Seat seat : seats) {
            if (seat.getStatus() != SeatStatus.AVAILABLE) {
                unavailableSeats.add(seat.getSeatNumber() + " (" + seat.getStatus() + ")");
            }
        }

        if (!unavailableSeats.isEmpty()) {
            throw new SeatNotAvailableException(
                    "The following seats are not available: " + String.join(", ", unavailableSeats));
        }

        // Calculate price with discount (if provided)
        DiscountCode discount = null;
        BigDecimal totalDiscountAmount = BigDecimal.ZERO;
        if (request.getDiscountCode() != null && !request.getDiscountCode().trim().isEmpty()) {
            discount = discountService.getValidDiscountCode(request.getDiscountCode());
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<SeatResponse> seatResponses = new ArrayList<>();

        for (Seat seat : seats) {
            BigDecimal seatPrice = calculateSeatPrice(seat, show, discount);
            totalAmount = totalAmount.add(seatPrice);

            // Calculate discount amount for this seat if discount is applied
            if (discount != null) {
                BigDecimal priceBeforeDiscount = calculateSeatPrice(seat, show, null);
                BigDecimal seatDiscountAmount = priceBeforeDiscount.subtract(seatPrice);
                totalDiscountAmount = totalDiscountAmount.add(seatDiscountAmount);
            }

            seatResponses.add(new SeatResponse(
                    seat.getId(),
                    seat.getSeatNumber(),
                    seat.getRowNumber(),
                    seat.getCategory(),
                    SeatStatus.HELD,
                    seatPrice
            ));
        }

        // Update seats to HELD status (optimistic locking will handle concurrency)
        for (Seat seat : seats) {
            seat.setStatus(SeatStatus.HELD);
        }
        seatRepository.saveAll(seats);

        // Create SeatHold with expiry = now + holdTimeoutMinutes
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryTime = now.plusMinutes(holdTimeoutMinutes);

        SeatHold seatHold = new SeatHold(
                userId,
                seats,
                expiryTime,
                HoldStatus.ACTIVE,
                now
        );
        seatHold = seatHoldRepository.save(seatHold);

        // Return HoldResponse
        return new HoldResponse(
                seatHold.getId(),
                expiryTime,
                totalAmount,
                seatResponses
        );
    }

    @Transactional
    public BookingResponse confirmBooking(String userId, ConfirmBookingRequest request) {
        // Get SeatHold by ID
        SeatHold seatHold = seatHoldRepository.findById(request.getHoldId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Seat hold not found with id: " + request.getHoldId()));

        // Validate userId matches
        if (!seatHold.getUserId().equals(userId)) {
            throw new ResourceNotFoundException(
                    "Seat hold not found with id: " + request.getHoldId());
        }

        // Check not expired
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(seatHold.getExpiryTime())) {
            throw new HoldExpiredException(
                    "Seat hold has expired at " + seatHold.getExpiryTime());
        }

        // Check status is ACTIVE
        if (seatHold.getStatus() != HoldStatus.ACTIVE) {
            throw new HoldExpiredException(
                    "Seat hold is not active. Current status: " + seatHold.getStatus());
        }

        // Get seats from hold
        List<Seat> seats = seatHold.getSeats();
        if (seats.isEmpty()) {
            throw new ResourceNotFoundException("No seats found in hold");
        }

        // Get show from first seat (all seats should belong to same show)
        Show show = seats.get(0).getShow();

        // Recalculate total amount (in case pricing changed, though unlikely)
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<SeatResponse> seatResponses = new ArrayList<>();

        for (Seat seat : seats) {
            // Note: For confirmation, we don't re-apply discount code
            // The price is already calculated during hold
            BigDecimal seatPrice = calculateSeatPrice(seat, show, null);
            totalAmount = totalAmount.add(seatPrice);

            seatResponses.add(new SeatResponse(
                    seat.getId(),
                    seat.getSeatNumber(),
                    seat.getRowNumber(),
                    seat.getCategory(),
                    SeatStatus.BOOKED,
                    seatPrice
            ));
        }

        // Update seats to BOOKED
        for (Seat seat : seats) {
            seat.setStatus(SeatStatus.BOOKED);
        }
        seatRepository.saveAll(seats);

        // Update SeatHold status to CONFIRMED
        seatHold.setStatus(HoldStatus.CONFIRMED);
        seatHoldRepository.save(seatHold);

        // Create Booking entity
        Booking booking = new Booking(
                userId,
                show,
                seats,
                totalAmount,
                BigDecimal.ZERO,  // discountApplied - can be enhanced later if needed
                null,  // discountCode
                now,
                BookingStatus.CONFIRMED,
                null  // refundAmount
        );
        booking = bookingRepository.save(booking);

        // Return BookingResponse
        return convertToBookingResponse(booking, seatResponses);
    }

    @Transactional
    public CancelBookingResponse cancelBooking(String userId, Long bookingId) {
        // Get Booking
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(
                        "Booking not found with id: " + bookingId));

        // Validate userId matches
        if (!booking.getUserId().equals(userId)) {
            throw new BookingNotFoundException(
                    "Booking not found with id: " + bookingId);
        }

        // Check status is CONFIRMED
        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new RefundNotAllowedException(
                    "Cannot cancel booking with status: " + booking.getStatus());
        }

        // Calculate refund based on time until show
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime showTime = booking.getShow().getShowTime();
        long hoursUntilShow = ChronoUnit.HOURS.between(now, showTime);

        BigDecimal refundPercentage;
        String message;

        if (hoursUntilShow >= 24) {
            refundPercentage = new BigDecimal("100");
            message = "Full refund granted (cancelled more than 24 hours before show)";
        } else if (hoursUntilShow >= 2) {
            refundPercentage = new BigDecimal("50");
            message = "50% refund granted (cancelled between 2-24 hours before show)";
        } else {
            refundPercentage = BigDecimal.ZERO;
            message = "No refund available (cancelled less than 2 hours before show)";
        }

        BigDecimal refundAmount = booking.getTotalAmount()
                .multiply(refundPercentage)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        // Update Booking: status=CANCELLED, refundAmount
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setRefundAmount(refundAmount);
        bookingRepository.save(booking);

        // Update seats back to AVAILABLE
        List<Seat> seats = booking.getSeats();
        for (Seat seat : seats) {
            seat.setStatus(SeatStatus.AVAILABLE);
        }
        seatRepository.saveAll(seats);

        // Return CancelBookingResponse
        return new CancelBookingResponse(
                bookingId,
                refundAmount,
                refundPercentage,
                message
        );
    }

    public List<Booking> getMyBookings(String userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Transactional
    public void releaseHoldManually(String userId, Long holdId) {
        // Get SeatHold by ID
        SeatHold seatHold = seatHoldRepository.findById(holdId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Seat hold not found with id: " + holdId));

        // Validate userId matches
        if (!seatHold.getUserId().equals(userId)) {
            throw new ResourceNotFoundException(
                    "Seat hold not found with id: " + holdId);
        }

        // Release seats from hold - update seats to AVAILABLE
        List<Seat> seats = seatHold.getSeats();
        for (Seat seat : seats) {
            if (seat.getStatus() == SeatStatus.HELD) {
                seat.setStatus(SeatStatus.AVAILABLE);
            }
        }
        seatRepository.saveAll(seats);

        // Update hold to EXPIRED
        seatHold.setStatus(HoldStatus.EXPIRED);
        seatHoldRepository.save(seatHold);
    }

    /**
     * Calculates the price for a single seat based on category, weekend multiplier, and discount.
     */
    private BigDecimal calculateSeatPrice(Seat seat, Show show, DiscountCode discount) {
        // Get base price based on seat category
        BigDecimal seatBasePrice = (seat.getCategory() == SeatCategory.PREMIUM)
                ? show.getBasePricePremium()
                : show.getBasePriceRegular();

        // Check if show is on weekend
        LocalDateTime showTime = show.getShowTime();
        boolean isWeekend = showTime.getDayOfWeek() == DayOfWeek.SATURDAY
                || showTime.getDayOfWeek() == DayOfWeek.SUNDAY;

        // Apply weekend multiplier
        BigDecimal weekendPrice = seatBasePrice.multiply(
                isWeekend ? show.getWeekendMultiplier() : BigDecimal.ONE
        );

        // Apply discount if provided
        if (discount != null) {
            BigDecimal percentageOff = new BigDecimal(discount.getPercentageOff());
            BigDecimal discountAmount = weekendPrice.multiply(percentageOff)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            BigDecimal finalPrice = weekendPrice.subtract(discountAmount);
            return finalPrice.setScale(2, RoundingMode.HALF_UP);
        }

        return weekendPrice.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Converts Booking entity to BookingResponse DTO.
     */
    private BookingResponse convertToBookingResponse(Booking booking, List<SeatResponse> seatResponses) {
        Show show = booking.getShow();

        // Build ShowResponse
        ShowResponse.MovieDetailsResponse movieDetails = new ShowResponse.MovieDetailsResponse(
                show.getMovie().getId(),
                show.getMovie().getTitle(),
                show.getMovie().getDurationMinutes()
        );

        ShowResponse.TheaterDetailsResponse theaterDetails = new ShowResponse.TheaterDetailsResponse(
                show.getTheater().getId(),
                show.getTheater().getName(),
                show.getTheater().getAddress()
        );

        ShowResponse showResponse = new ShowResponse(
                show.getId(),
                movieDetails,
                theaterDetails,
                show.getShowTime(),
                show.getBasePriceRegular(),
                show.getBasePricePremium(),
                show.getWeekendMultiplier()
        );

        return new BookingResponse(
                booking.getId(),
                showResponse,
                seatResponses,
                booking.getTotalAmount(),
                booking.getDiscountApplied(),
                booking.getDiscountCode(),
                booking.getBookingTime(),
                booking.getStatus()
        );
    }
}
