package com.moviebooking.entity;

import com.moviebooking.enums.HoldStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seat_holds")
public class SeatHold {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "User ID cannot be blank")
    @Column(nullable = false)
    private String userId;

    @OneToMany
    @JoinColumn(name = "hold_id")
    private List<Seat> seats = new ArrayList<>();

    @NotNull(message = "Expiry time cannot be null")
    @Column(nullable = false)
    private LocalDateTime expiryTime;

    @NotNull(message = "Hold status cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HoldStatus status = HoldStatus.ACTIVE;

    @NotNull(message = "Created at cannot be null")
    @Column(nullable = false)
    private LocalDateTime createdAt;

    public SeatHold() {
    }

    public SeatHold(String userId, List<Seat> seats, LocalDateTime expiryTime,
                    HoldStatus status, LocalDateTime createdAt) {
        this.userId = userId;
        this.seats = seats;
        this.expiryTime = expiryTime;
        this.status = status;
        this.createdAt = createdAt;
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

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public HoldStatus getStatus() {
        return status;
    }

    public void setStatus(HoldStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "SeatHold{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", seats=" + seats +
                ", expiryTime=" + expiryTime +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
