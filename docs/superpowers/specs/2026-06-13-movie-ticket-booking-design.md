# Movie Ticket Booking System - Design Specification

**Date:** 2026-06-13  
**Project:** SDE-2 Take-Home Assignment  
**Stack:** Spring Boot 3.x, MySQL 8.x, Java 17+

---

## Overview

A simple movie ticket booking system supporting multiple cities, theaters, shows, and seat-level booking with time-bound holds, pricing tiers, discount codes, and refund policies. The system handles concurrent booking attempts without double-allocation using optimistic locking.

## Core Requirements

### Functional Requirements
- Multiple cities with multiple theaters per city
- Multiple shows per theater with seat-level booking
- Seat selection with time-bound holds (10 minutes default, auto-release on expiry)
- Multiple pricing tiers: regular seats, premium seats, weekend multiplier
- Discount codes with percentage-based discounts
- Payment and booking confirmation
- Cancellation with configurable time-based refund policies
- Concurrent booking handling without double-allocation

### Roles
- **Admin**: Manage cities, theaters, shows, seat layouts, pricing tiers, discount codes
- **Customer**: Browse shows, book/cancel seats, view booking history

### Non-Functional Requirements
- Optimistic locking for concurrency control
- Scheduled job for automatic seat hold expiry
- Input validation and error handling
- Unit and integration tests for core flows

---

## Architecture

### Approach
Simple layered architecture with Controller → Service → Repository pattern.

### Technology Stack
- **Framework**: Spring Boot 3.x
- **Database**: MySQL 8.x
- **ORM**: Spring Data JPA (Hibernate)
- **Security**: Spring Security with in-memory users (basic HTTP auth)
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito, Spring Boot Test
- **Java Version**: 17+

### In-Memory Users
- **Admin**: username=`admin`, password=`admin`, role=`ADMIN`
- **Customer**: username=`customer`, password=`customer`, role=`CUSTOMER`

---

## Data Model

### Entities

#### City
- `id` (Long, PK)
- `name` (String, unique)

#### Theater
- `id` (Long, PK)
- `name` (String)
- `address` (String)
- `city` (ManyToOne → City)

#### SeatLayout
- `id` (Long, PK)
- `theater` (OneToOne → Theater)
- `rows` (Integer)
- `seatsPerRow` (Integer)
- `premiumRows` (String, comma-separated row numbers, e.g., "1,2,3")

#### Movie
- `id` (Long, PK)
- `title` (String)
- `description` (String)
- `durationMinutes` (Integer)
- `genre` (String)
- `language` (String)

#### Show
- `id` (Long, PK)
- `movie` (ManyToOne → Movie)
- `theater` (ManyToOne → Theater)
- `showTime` (LocalDateTime)
- `basePriceRegular` (BigDecimal)
- `basePricePremium` (BigDecimal)
- `weekendMultiplier` (BigDecimal, default 1.0)

#### Seat
- `id` (Long, PK)
- `show` (ManyToOne → Show)
- `seatNumber` (String, e.g., "A1")
- `rowNumber` (String)
- `category` (Enum: REGULAR, PREMIUM)
- `status` (Enum: AVAILABLE, HELD, BOOKED)
- `version` (Long, for optimistic locking)

#### SeatHold
- `id` (Long, PK)
- `userId` (String, username)
- `seats` (OneToMany → Seat)
- `expiryTime` (LocalDateTime)
- `status` (Enum: ACTIVE, EXPIRED, CONFIRMED)
- `createdAt` (LocalDateTime)

#### Booking
- `id` (Long, PK)
- `userId` (String, username)
- `show` (ManyToOne → Show)
- `seats` (OneToMany → Seat)
- `totalAmount` (BigDecimal)
- `discountApplied` (BigDecimal)
- `discountCode` (String, nullable)
- `bookingTime` (LocalDateTime)
- `status` (Enum: CONFIRMED, CANCELLED)
- `refundAmount` (BigDecimal, nullable)

#### DiscountCode
- `id` (Long, PK)
- `code` (String, unique)
- `percentageOff` (Integer, 0-100)
- `validFrom` (LocalDateTime)
- `validUntil` (LocalDateTime)
- `active` (Boolean)

---

## API Endpoints

### Admin Endpoints (Role: ADMIN)

#### City Management
- `POST /api/admin/cities` - Create city
  - Request: `{name}`
  - Response: City object

- `GET /api/admin/cities` - List all cities
  - Response: List of cities

#### Theater Management
- `POST /api/admin/theaters` - Create theater
  - Request: `{name, address, cityId}`
  - Response: Theater object

- `GET /api/admin/theaters?cityId={id}` - List theaters by city
  - Response: List of theaters

- `POST /api/admin/theaters/{id}/seat-layout` - Configure seat layout
  - Request: `{rows, seatsPerRow, premiumRows}`
  - Response: SeatLayout object

#### Movie Management
- `POST /api/admin/movies` - Create movie
  - Request: `{title, description, durationMinutes, genre, language}`
  - Response: Movie object

- `GET /api/admin/movies` - List all movies
  - Response: List of movies

#### Show Management
- `POST /api/admin/shows` - Create show (auto-generates seats from layout)
  - Request: `{movieId, theaterId, showTime, basePriceRegular, basePricePremium, weekendMultiplier}`
  - Response: Show object with seat count

- `PUT /api/admin/shows/{id}/pricing` - Update pricing
  - Request: `{basePriceRegular, basePricePremium, weekendMultiplier}`
  - Response: Updated show

#### Discount Management
- `POST /api/admin/discounts` - Create discount code
  - Request: `{code, percentageOff, validFrom, validUntil}`
  - Response: DiscountCode object

- `GET /api/admin/discounts` - List all discount codes
  - Response: List of discount codes

- `DELETE /api/admin/discounts/{id}` - Deactivate discount code
  - Response: 204 No Content

### Customer Endpoints (Role: CUSTOMER)

#### Show Browsing
- `GET /api/shows?cityId={id}&date={date}&movieId={id}` - Browse shows
  - Query params: cityId (optional), date (optional, format: yyyy-MM-dd), movieId (optional)
  - Response: List of shows with movie and theater details

- `GET /api/shows/{id}/seats` - View available seats for a show
  - Response: List of seats with status, category, and price

#### Booking Flow
- `POST /api/bookings/hold` - Hold seats temporarily
  - Request: `{showId, seatIds[], discountCode (optional)}`
  - Response: `{holdId, expiryTime, totalAmount, seats[]}`

- `POST /api/bookings/confirm` - Confirm booking and process payment
  - Request: `{holdId, paymentMethod}`
  - Response: Booking object with confirmation details

- `DELETE /api/bookings/hold/{id}` - Release held seats manually
  - Response: 204 No Content

#### Booking Management
- `GET /api/bookings/my-bookings` - View booking history
  - Response: List of bookings for current user

- `POST /api/bookings/{id}/cancel` - Cancel booking with refund
  - Response: `{refundAmount, refundPercentage, message}`

#### Discount Validation
- `POST /api/discounts/validate` - Validate discount code
  - Request: `{code}`
  - Response: `{valid, percentageOff, expiryDate}`

---

## Business Logic

### Pricing Calculation
```
seatBasePrice = seat.category == PREMIUM ? show.basePricePremium : show.basePriceRegular
weekendPrice = seatBasePrice × show.weekendMultiplier (if show is on Sat/Sun)
discountAmount = weekendPrice × (discountCode.percentageOff / 100)
finalPrice = weekendPrice - discountAmount
totalAmount = sum of all seat finalPrices
```

### Seat Hold Flow
1. Customer selects seats and calls `POST /api/bookings/hold`
2. System validates seats are AVAILABLE
3. Attempts to update seat status to HELD using optimistic locking (@Version)
4. If successful, creates SeatHold record with expiryTime = now + 10 minutes
5. Returns hold details with total amount (including discount if provided)
6. Customer has 10 minutes to call `POST /api/bookings/confirm`
7. On confirm: seats become BOOKED, SeatHold status = CONFIRMED, Booking created
8. On expiry: scheduled job releases seats back to AVAILABLE, SeatHold status = EXPIRED

### Concurrency Handling
- **Optimistic Locking**: Each Seat has `@Version` field
- When two users try to hold/book same seat concurrently:
  - First transaction succeeds
  - Second transaction gets `OptimisticLockException`
  - Caught and returned as "Seat no longer available" error
- Transaction boundaries: All booking operations wrapped in `@Transactional`

### Seat Hold Expiry (Scheduled Job)
- Runs every 60 seconds: `@Scheduled(fixedRate = 60000)`
- Finds all SeatHold records where:
  - `status = ACTIVE`
  - `expiryTime < now()`
- For each expired hold:
  - Update all associated seats: status = AVAILABLE
  - Update SeatHold: status = EXPIRED

### Refund Policy
Time-based refund calculation on cancellation:
- **≥24 hours before show**: 100% refund
- **2-24 hours before show**: 50% refund
- **<2 hours before show**: 0% refund (no refund)

Calculation:
```
hoursUntilShow = (show.showTime - now) in hours
if (hoursUntilShow >= 24) refundPercentage = 100
else if (hoursUntilShow >= 2) refundPercentage = 50
else refundPercentage = 0
refundAmount = booking.totalAmount × (refundPercentage / 100)
```

On cancellation:
- Update Booking: status = CANCELLED, refundAmount = calculated amount
- Update Seats: status = AVAILABLE
- Return refund details to customer

---

## Project Structure

```
movie-ticket-booking/
├── src/main/java/com/moviebooking/
│   ├── MovieBookingApplication.java
│   ├── config/
│   │   ├── SecurityConfig.java          (in-memory users, HTTP basic auth)
│   │   └── SchedulingConfig.java        (@EnableScheduling)
│   ├── controller/
│   │   ├── AdminController.java         (admin endpoints)
│   │   └── BookingController.java       (customer endpoints)
│   ├── service/
│   │   ├── CityService.java
│   │   ├── TheaterService.java
│   │   ├── MovieService.java
│   │   ├── ShowService.java             (creates seats from layout)
│   │   ├── BookingService.java          (hold, confirm, cancel logic)
│   │   ├── DiscountService.java
│   │   └── SeatReleaseScheduler.java    (scheduled job)
│   ├── repository/
│   │   ├── CityRepository.java
│   │   ├── TheaterRepository.java
│   │   ├── SeatLayoutRepository.java
│   │   ├── MovieRepository.java
│   │   ├── ShowRepository.java
│   │   ├── SeatRepository.java
│   │   ├── SeatHoldRepository.java
│   │   ├── BookingRepository.java
│   │   └── DiscountCodeRepository.java
│   ├── entity/
│   │   ├── City.java
│   │   ├── Theater.java
│   │   ├── SeatLayout.java
│   │   ├── Movie.java
│   │   ├── Show.java
│   │   ├── Seat.java                     (@Version for optimistic locking)
│   │   ├── SeatHold.java
│   │   ├── Booking.java
│   │   └── DiscountCode.java
│   ├── dto/
│   │   ├── request/
│   │   │   ├── CreateCityRequest.java
│   │   │   ├── CreateTheaterRequest.java
│   │   │   ├── CreateShowRequest.java
│   │   │   ├── HoldSeatsRequest.java
│   │   │   ├── ConfirmBookingRequest.java
│   │   │   └── ... (other request DTOs)
│   │   └── response/
│   │       ├── ShowResponse.java
│   │       ├── SeatResponse.java
│   │       ├── HoldResponse.java
│   │       ├── BookingResponse.java
│   │       └── ... (other response DTOs)
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java   (@ControllerAdvice)
│   │   ├── SeatNotAvailableException.java
│   │   ├── InvalidDiscountException.java
│   │   ├── BookingNotFoundException.java
│   │   ├── HoldExpiredException.java
│   │   └── RefundNotAllowedException.java
│   └── enums/
│       ├── SeatCategory.java             (REGULAR, PREMIUM)
│       ├── SeatStatus.java               (AVAILABLE, HELD, BOOKED)
│       ├── HoldStatus.java               (ACTIVE, EXPIRED, CONFIRMED)
│       └── BookingStatus.java            (CONFIRMED, CANCELLED)
├── src/main/resources/
│   ├── application.properties            (MySQL config, JPA settings)
│   └── data.sql                          (optional seed data)
├── src/test/java/com/moviebooking/
│   ├── integration/
│   │   ├── AdminControllerIntegrationTest.java
│   │   └── BookingControllerIntegrationTest.java
│   └── service/
│       ├── BookingServiceTest.java
│       ├── ShowServiceTest.java
│       └── SeatReleaseSchedulerTest.java
├── pom.xml
└── README.md
```

---

## Error Handling

### Validation
- Use `@Valid` on request DTOs
- Bean Validation annotations: `@NotNull`, `@NotBlank`, `@Min`, `@Max`, `@Future`, etc.
- Custom validators for business rules

### Exception Handling
`GlobalExceptionHandler` with `@ControllerAdvice` catches:
- `MethodArgumentNotValidException` → 400 Bad Request (validation errors)
- `OptimisticLockException` → 409 Conflict (seat already taken)
- `SeatNotAvailableException` → 409 Conflict
- `InvalidDiscountException` → 400 Bad Request
- `BookingNotFoundException` → 404 Not Found
- `HoldExpiredException` → 410 Gone
- `RefundNotAllowedException` → 400 Bad Request
- `AccessDeniedException` → 403 Forbidden
- Generic `Exception` → 500 Internal Server Error

### Error Response Format
```json
{
  "timestamp": "2026-06-13T22:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "Seat A5 is no longer available",
  "path": "/api/bookings/hold"
}
```

---

## Testing Strategy

### Unit Tests
**Service Layer Tests** (Mockito for repositories):
- `BookingServiceTest`: hold seats, confirm booking, cancel with refund calculation
- `ShowServiceTest`: create show with seats from layout, pricing calculation
- `DiscountServiceTest`: validate discount code, apply discount
- `SeatReleaseSchedulerTest`: mock time and verify seat release logic

**Test Coverage:**
- Pricing calculation (regular/premium, weekend, discount)
- Refund calculation based on time windows
- Seat hold expiry logic
- Discount validation (active, date range)

### Integration Tests
**API Tests** (`@SpringBootTest`, `@AutoConfigureMockMvc`):
- `AdminControllerIntegrationTest`: 
  - Create cities, theaters, shows
  - Configure seat layouts
  - Manage discount codes
  
- `BookingControllerIntegrationTest`:
  - Browse shows with filters
  - Hold seats → confirm booking flow
  - Hold seats → expiry → seats released
  - Concurrent booking (two users, same seat, one fails)
  - Cancel booking with different refund scenarios
  - Apply discount code

**Test Database:**
- Use H2 in-memory database for tests (`application-test.properties`)
- `@Transactional` on test methods for automatic rollback

### Concurrent Booking Test
```java
@Test
void testConcurrentBooking() throws Exception {
    // Two threads try to book seat A1 simultaneously
    // Thread 1: hold seats, should succeed
    // Thread 2: hold same seats, should fail with 409 Conflict
    // Verify only one booking created
}
```

### Test Data Setup
- Use `@BeforeEach` to create test data: cities, theaters, shows, seats
- Helper methods for common setups
- Test both success and failure scenarios

---

## Configuration

### application.properties
```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/movie_booking
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Scheduling
seat.hold.timeout.minutes=10

# Security
spring.security.user.name=admin
spring.security.user.password=admin
```

### SecurityConfig
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User.withUsername("admin")
            .password("{noop}admin")
            .roles("ADMIN")
            .build();
        UserDetails customer = User.withUsername("customer")
            .password("{noop}customer")
            .roles("CUSTOMER")
            .build();
        return new InMemoryUserDetailsManager(admin, customer);
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        return http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/**").hasAnyRole("CUSTOMER", "ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .build();
    }
}
```

---

## Assumptions & Scope

### In Scope
- REST APIs for all core flows
- MySQL database persistence
- Basic HTTP authentication with in-memory users
- Optimistic locking for seat booking concurrency
- Scheduled job for seat hold expiry
- Time-based refund policies
- Percentage-based discount codes
- Input validation and error handling
- Unit and integration tests

### Out of Scope (Explicitly Excluded)
- UI/Frontend
- Deployment, containerization, CI/CD
- Distributed systems, microservices
- Advanced authentication (OAuth, JWT, SSO, MFA)
- Production observability, monitoring, alerting
- Email/SMS notifications (removed for simplicity)
- Payment gateway integration (simulated)
- Advanced discount types (fixed amount, BOGO, etc.)

### Key Assumptions
1. **Seat Layout**: Simple grid layout (rows × seatsPerRow), premium rows defined at theater level
2. **User Management**: In-memory users only, no registration/profile management
3. **Payment**: Simulated, no real payment gateway
4. **Notifications**: Removed entirely for simplicity
5. **Weekend Detection**: Saturday and Sunday only
6. **Timezone**: System timezone used for all date/time operations
7. **Single Currency**: All prices in one currency (no multi-currency support)
8. **Seat Hold Timeout**: Configurable in properties, default 10 minutes
9. **Refund Processing**: Immediate, no approval workflow
10. **Discount Limits**: One discount code per booking, no stacking

### Database Setup Required
- MySQL 8.x installed and running on localhost:3306
- Database `movie_booking` created
- User `root` with password `root` (or update application.properties)

---

## Success Criteria

1. ✅ Admin can create cities, theaters, movies, shows, and discount codes
2. ✅ Admin can configure seat layouts for theaters
3. ✅ Seats auto-generated from layout when show is created
4. ✅ Customers can browse shows with filters (city, date, movie)
5. ✅ Customers can hold seats with automatic expiry
6. ✅ Concurrent booking attempts handled correctly (no double-booking)
7. ✅ Discount codes validated and applied correctly
8. ✅ Pricing calculated with category, weekend multiplier, and discount
9. ✅ Bookings can be cancelled with time-based refund calculation
10. ✅ Unit tests cover service logic
11. ✅ Integration tests cover API flows
12. ✅ Proper error handling and validation throughout

---

## Implementation Notes

### Critical Implementation Details
1. **Seat Generation**: When creating a show, generate all seats based on theater's SeatLayout
2. **Optimistic Locking**: Always use `@Version` on Seat entity and handle `OptimisticLockException`
3. **Transaction Management**: Mark service methods with `@Transactional` where state changes occur
4. **Scheduled Job**: Use `@Scheduled(fixedRate = 60000)` with proper transaction handling
5. **Weekend Detection**: `showTime.getDayOfWeek() == SATURDAY || showTime.getDayOfWeek() == SUNDAY`
6. **Discount Validation**: Check active flag, validFrom/validUntil dates before applying
7. **Refund Calculation**: Always calculate based on current time vs show time, not booking time

### Common Pitfalls to Avoid
- Not handling optimistic lock exceptions properly
- Forgetting to release seats when hold expires
- Not using transactions for multi-step operations
- Missing validation on date/time fields
- Not testing concurrent scenarios
- Hardcoding timeout values instead of using configuration

---

## Next Steps

After design approval:
1. Generate Spring Boot project structure with Maven
2. Implement entities with JPA annotations
3. Create repositories
4. Implement service layer with business logic
5. Create controllers with REST endpoints
6. Configure security and scheduling
7. Write unit tests
8. Write integration tests
9. Create README with setup instructions
10. Create sample data script
11. Test end-to-end flows
12. Commit to GitHub with multiple commits showing development progress
