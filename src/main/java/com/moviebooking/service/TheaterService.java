package com.moviebooking.service;

import com.moviebooking.dto.request.CreateSeatLayoutRequest;
import com.moviebooking.dto.request.CreateTheaterRequest;
import com.moviebooking.entity.City;
import com.moviebooking.entity.SeatLayout;
import com.moviebooking.entity.Theater;
import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.repository.CityRepository;
import com.moviebooking.repository.SeatLayoutRepository;
import com.moviebooking.repository.TheaterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TheaterService {

    private final TheaterRepository theaterRepository;
    private final CityRepository cityRepository;
    private final SeatLayoutRepository seatLayoutRepository;

    public TheaterService(TheaterRepository theaterRepository,
                         CityRepository cityRepository,
                         SeatLayoutRepository seatLayoutRepository) {
        this.theaterRepository = theaterRepository;
        this.cityRepository = cityRepository;
        this.seatLayoutRepository = seatLayoutRepository;
    }

    @Transactional
    public Theater createTheater(CreateTheaterRequest request) {
        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + request.getCityId()));

        Theater theater = new Theater(request.getName(), request.getAddress(), city);
        return theaterRepository.save(theater);
    }

    public List<Theater> getTheatersByCityId(Long cityId) {
        return theaterRepository.findByCityId(cityId);
    }

    public List<Theater> getAllTheaters() {
        return theaterRepository.findAll();
    }

    public Theater getTheaterById(Long id) {
        return theaterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + id));
    }

    @Transactional
    public SeatLayout createSeatLayout(Long theaterId, CreateSeatLayoutRequest request) {
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + theaterId));

        SeatLayout seatLayout = new SeatLayout(
                theater,
                request.getRows(),
                request.getSeatsPerRow(),
                request.getPremiumRows()
        );
        return seatLayoutRepository.save(seatLayout);
    }
}
