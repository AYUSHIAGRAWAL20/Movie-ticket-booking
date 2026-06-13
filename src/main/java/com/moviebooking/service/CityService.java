package com.moviebooking.service;

import com.moviebooking.dto.request.CreateCityRequest;
import com.moviebooking.entity.City;
import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.repository.CityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Transactional
    public City createCity(CreateCityRequest request) {
        City city = new City(request.getName());
        return cityRepository.save(city);
    }

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public City getCityById(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));
    }
}
