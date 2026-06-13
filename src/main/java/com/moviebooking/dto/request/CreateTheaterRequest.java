package com.moviebooking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateTheaterRequest {

    @NotBlank(message = "Theater name is required")
    private String name;

    @NotBlank(message = "Theater address is required")
    private String address;

    @NotNull(message = "City ID is required")
    private Long cityId;

    public CreateTheaterRequest() {
    }

    public CreateTheaterRequest(String name, String address, Long cityId) {
        this.name = name;
        this.address = address;
        this.cityId = cityId;
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

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }
}
