package com.moviebooking.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CreateCityRequest {

    @NotBlank(message = "City name is required")
    private String name;

    public CreateCityRequest() {
    }

    public CreateCityRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
