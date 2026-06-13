package com.moviebooking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "discount_codes")
public class DiscountCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Discount code cannot be blank")
    @Column(nullable = false, unique = true)
    private String code;

    @NotNull(message = "Percentage off cannot be null")
    @Min(value = 0, message = "Percentage off must be at least 0")
    @Max(value = 100, message = "Percentage off cannot exceed 100")
    @Column(nullable = false)
    private Integer percentageOff;

    @NotNull(message = "Valid from cannot be null")
    @Column(nullable = false)
    private LocalDateTime validFrom;

    @NotNull(message = "Valid until cannot be null")
    @Column(nullable = false)
    private LocalDateTime validUntil;

    @NotNull(message = "Active status cannot be null")
    @Column(nullable = false)
    private Boolean active = true;

    public DiscountCode() {
    }

    public DiscountCode(String code, Integer percentageOff,
                        LocalDateTime validFrom, LocalDateTime validUntil,
                        Boolean active) {
        this.code = code;
        this.percentageOff = percentageOff;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getPercentageOff() {
        return percentageOff;
    }

    public void setPercentageOff(Integer percentageOff) {
        this.percentageOff = percentageOff;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "DiscountCode{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", percentageOff=" + percentageOff +
                ", validFrom=" + validFrom +
                ", validUntil=" + validUntil +
                ", active=" + active +
                '}';
    }
}
