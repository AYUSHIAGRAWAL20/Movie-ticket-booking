package com.moviebooking.config;

import com.moviebooking.dto.request.*;
import com.moviebooking.entity.*;
import com.moviebooking.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Component
@Order(1)
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final CityService cityService;
    private final TheaterService theaterService;
    private final MovieService movieService;
    private final ShowService showService;
    private final DiscountService discountService;

    public DataInitializer(CityService cityService,
                          TheaterService theaterService,
                          MovieService movieService,
                          ShowService showService,
                          DiscountService discountService) {
        this.cityService = cityService;
        this.theaterService = theaterService;
        this.movieService = movieService;
        this.showService = showService;
        this.discountService = discountService;
    }

    @Override
    public void run(String... args) {
        log.info("Starting sample data initialization...");

        try {
            // Create cities
            log.info("Creating cities...");
            City mumbai = cityService.createCity(new CreateCityRequest("Mumbai"));
            City delhi = cityService.createCity(new CreateCityRequest("Delhi"));
            log.info("Created cities: Mumbai (ID: {}), Delhi (ID: {})", mumbai.getId(), delhi.getId());

            // Create theaters
            log.info("Creating theaters...");
            Theater pvrMumbai = theaterService.createTheater(
                    new CreateTheaterRequest("PVR Cinemas Mumbai", "Phoenix Mall, Lower Parel, Mumbai", mumbai.getId())
            );
            Theater inoxDelhi = theaterService.createTheater(
                    new CreateTheaterRequest("INOX Delhi", "Select Citywalk, Saket, Delhi", delhi.getId())
            );
            log.info("Created theaters: PVR Cinemas Mumbai (ID: {}), INOX Delhi (ID: {})",
                    pvrMumbai.getId(), inoxDelhi.getId());

            // Create seat layouts
            log.info("Creating seat layouts...");
            SeatLayout pvrLayout = theaterService.createSeatLayout(
                    pvrMumbai.getId(),
                    new CreateSeatLayoutRequest(10, 15, "1,2,3")
            );
            SeatLayout inoxLayout = theaterService.createSeatLayout(
                    inoxDelhi.getId(),
                    new CreateSeatLayoutRequest(10, 15, "1,2,3")
            );
            log.info("Created seat layouts for both theaters (10 rows x 15 seats, premium rows: 1-3)");

            // Create movies
            log.info("Creating movies...");
            Movie inception = movieService.createMovie(
                    new CreateMovieRequest(
                            "Inception",
                            "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.",
                            148,
                            "Sci-Fi",
                            "English"
                    )
            );
            Movie darkKnight = movieService.createMovie(
                    new CreateMovieRequest(
                            "The Dark Knight",
                            "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.",
                            152,
                            "Action",
                            "English"
                    )
            );
            Movie interstellar = movieService.createMovie(
                    new CreateMovieRequest(
                            "Interstellar",
                            "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.",
                            169,
                            "Sci-Fi",
                            "English"
                    )
            );
            log.info("Created movies: Inception (ID: {}), The Dark Knight (ID: {}), Interstellar (ID: {})",
                    inception.getId(), darkKnight.getId(), interstellar.getId());

            // Create shows
            log.info("Creating shows...");
            LocalDateTime now = LocalDateTime.now();

            // Tomorrow's shows at different times
            LocalDateTime tomorrowNoon = now.plusDays(1).withHour(12).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime tomorrowAfternoon = now.plusDays(1).withHour(15).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime tomorrowEvening = now.plusDays(1).withHour(18).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime tomorrowNight = now.plusDays(1).withHour(21).withMinute(0).withSecond(0).withNano(0);

            // Determine if tomorrow is a weekend
            DayOfWeek tomorrowDayOfWeek = tomorrowNoon.getDayOfWeek();
            BigDecimal weekendMultiplier = (tomorrowDayOfWeek == DayOfWeek.SATURDAY || tomorrowDayOfWeek == DayOfWeek.SUNDAY)
                    ? new BigDecimal("1.2")
                    : new BigDecimal("1.0");

            // Show 1: Inception at PVR Mumbai - Tomorrow Noon
            Show show1 = showService.createShow(
                    new CreateShowRequest(
                            inception.getId(),
                            pvrMumbai.getId(),
                            tomorrowNoon,
                            new BigDecimal("200"),
                            new BigDecimal("350"),
                            weekendMultiplier
                    )
            );

            // Show 2: The Dark Knight at INOX Delhi - Tomorrow Afternoon
            Show show2 = showService.createShow(
                    new CreateShowRequest(
                            darkKnight.getId(),
                            inoxDelhi.getId(),
                            tomorrowAfternoon,
                            new BigDecimal("200"),
                            new BigDecimal("350"),
                            weekendMultiplier
                    )
            );

            // Show 3: Interstellar at PVR Mumbai - Tomorrow Evening
            Show show3 = showService.createShow(
                    new CreateShowRequest(
                            interstellar.getId(),
                            pvrMumbai.getId(),
                            tomorrowEvening,
                            new BigDecimal("200"),
                            new BigDecimal("350"),
                            weekendMultiplier
                    )
            );

            // Show 4: Inception at INOX Delhi - Tomorrow Night
            Show show4 = showService.createShow(
                    new CreateShowRequest(
                            inception.getId(),
                            inoxDelhi.getId(),
                            tomorrowNight,
                            new BigDecimal("200"),
                            new BigDecimal("350"),
                            weekendMultiplier
                    )
            );

            log.info("Created 4 shows for tomorrow at different times");
            log.info("Show times: 12:00, 15:00, 18:00, 21:00");
            log.info("Weekend multiplier applied: {} (Is weekend: {})",
                    weekendMultiplier,
                    tomorrowDayOfWeek == DayOfWeek.SATURDAY || tomorrowDayOfWeek == DayOfWeek.SUNDAY);

            // Create discount codes
            log.info("Creating discount codes...");
            LocalDateTime validFrom = now;
            LocalDateTime validUntil = now.plusDays(30);

            DiscountCode save20 = discountService.createDiscountCode(
                    new CreateDiscountCodeRequest(
                            "SAVE20",
                            20,
                            validFrom,
                            validUntil
                    )
            );

            DiscountCode weekend10 = discountService.createDiscountCode(
                    new CreateDiscountCodeRequest(
                            "WEEKEND10",
                            10,
                            validFrom,
                            validUntil
                    )
            );

            log.info("Created discount codes: SAVE20 (20% off), WEEKEND10 (10% off)");
            log.info("Discount codes valid until: {}", validUntil.toLocalDate());

            log.info("==============================================");
            log.info("Sample data initialized successfully!");
            log.info("==============================================");
            log.info("Summary:");
            log.info("  - Cities: 2 (Mumbai, Delhi)");
            log.info("  - Theaters: 2 (PVR Cinemas Mumbai, INOX Delhi)");
            log.info("  - Seat Layouts: 10 rows x 15 seats with premium rows 1-3");
            log.info("  - Movies: 3 (Inception, The Dark Knight, Interstellar)");
            log.info("  - Shows: 4 (scheduled for tomorrow at various times)");
            log.info("  - Discount Codes: 2 (SAVE20, WEEKEND10)");
            log.info("==============================================");

        } catch (Exception e) {
            log.error("Error initializing sample data: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize sample data", e);
        }
    }
}
