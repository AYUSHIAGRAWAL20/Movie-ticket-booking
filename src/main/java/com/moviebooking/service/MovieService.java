package com.moviebooking.service;

import com.moviebooking.dto.request.CreateMovieRequest;
import com.moviebooking.entity.Movie;
import com.moviebooking.exception.ResourceNotFoundException;
import com.moviebooking.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Transactional
    public Movie createMovie(CreateMovieRequest request) {
        Movie movie = new Movie(
                request.getTitle(),
                request.getDescription(),
                request.getDurationMinutes(),
                request.getGenre(),
                request.getLanguage()
        );
        return movieRepository.save(movie);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
    }
}
