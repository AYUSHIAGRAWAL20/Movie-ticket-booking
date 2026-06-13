package com.moviebooking.entity;

import com.moviebooking.enums.BookingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "User ID cannot be blank")
    @Column(nullable = false)
    private String userId;

    @ManyToOne
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @OneToMany
    @JoinColumn(name = "booking_id")
    private List<Seat> seats = new ArrayList<>();

    @NotNull(message = "Total amount cannot be null")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal discountApplied;

    @Column(nullable = true)
    private String discountCode;

    @NotNull(message = "Booking time cannot be null")
    @Column(nullable = false)
    private LocalDateTime bookingTime;

    @NotNull(message = "Booking status cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.CONFIRMED;

    @Column(precision = 10, scale = 2)
    private BigDecimal refundAmount;

    public Booking() {
    }

    public Booking(String userId, Show show, List<Seat> seats,
                   BigDecimal totalAmount, BigDecimal discountApplied,
                   String discountCode, LocalDateTime bookingTime,
                   BookingStatus status, BigDecimal refundAmount) {
        this.userId = userId;
        this.show = show;
        this.seats = seats;
        this.totalAmount = totalAmount;
        this.discountApplied = discountApplied;
        this.discountCode = discountCode;
        this.bookingTime = bookingTime;
        this.status = status;
        this.refundAmount = refundAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
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

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", show=" + show +
                ", seats=" + seats +
                ", totalAmount=" + totalAmount +
                ", discountApplied=" + discountApplied +
                ", discountCode='" + discountCode + '\'' +
                ", bookingTime=" + bookingTime +
                ", status=" + status +
                ", refundAmount=" + refundAmount +
                '}';
    }
}
