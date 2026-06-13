package com.moviebooking.entity;

import com.moviebooking.enums.SeatCategory;
import com.moviebooking.enums.SeatStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @NotBlank(message = "Seat number cannot be blank")
    @Column(nullable = false)
    private String seatNumber;

    @NotBlank(message = "Row number cannot be blank")
    @Column(nullable = false)
    private String rowNumber;

    @NotNull(message = "Seat category cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatCategory category;

    @NotNull(message = "Seat status cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus status = SeatStatus.AVAILABLE;

    @Version
    private Long version;

    public Seat() {
    }

    public Seat(Show show, String seatNumber, String rowNumber,
                SeatCategory category, SeatStatus status) {
        this.show = show;
        this.seatNumber = seatNumber;
        this.rowNumber = rowNumber;
        this.category = category;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(String rowNumber) {
        this.rowNumber = rowNumber;
    }

    public SeatCategory getCategory() {
        return category;
    }

    public void setCategory(SeatCategory category) {
        this.category = category;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", show=" + show +
                ", seatNumber='" + seatNumber + '\'' +
                ", rowNumber='" + rowNumber + '\'' +
                ", category=" + category +
                ", status=" + status +
                ", version=" + version +
                '}';
    }
}
