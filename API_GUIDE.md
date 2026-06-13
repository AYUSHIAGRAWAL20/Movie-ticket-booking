# API Usage Guide

Quick guide to test the Movie Ticket Booking System APIs.

## Authentication

The system uses HTTP Basic Authentication with two users:

- **Admin**: `admin` / `admin`
- **Customer**: `customer` / `customer`

## Testing with cURL

### Admin Endpoints

**1. List Cities**
```bash
curl -u admin:admin http://localhost:8080/api/admin/cities
```

**2. List Movies**
```bash
curl -u admin:admin http://localhost:8080/api/admin/movies
```

**3. List Theaters**
```bash
curl -u admin:admin http://localhost:8080/api/admin/theaters
```

**4. List Discount Codes**
```bash
curl -u admin:admin http://localhost:8080/api/admin/discounts
```

**5. Create a New Movie**
```bash
curl -u admin:admin -X POST http://localhost:8080/api/admin/movies \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Avengers: Endgame",
    "description": "Epic superhero conclusion",
    "durationMinutes": 181,
    "genre": "Action",
    "language": "English"
  }'
```

### Customer Endpoints

**1. Browse All Shows**
```bash
curl -u customer:customer http://localhost:8080/api/shows
```

**2. Browse Shows by City (Mumbai = 1)**
```bash
curl -u customer:customer "http://localhost:8080/api/shows?cityId=1"
```

**3. Browse Shows by Movie (Inception = 1)**
```bash
curl -u customer:customer "http://localhost:8080/api/shows?movieId=1"
```

**4. Get Seats for a Show (Show ID = 1)**
```bash
curl -u customer:customer http://localhost:8080/api/shows/1/seats
```

**5. Hold Seats (no discount)**
```bash
curl -u customer:customer -X POST http://localhost:8080/api/bookings/hold \
  -H "Content-Type: application/json" \
  -d '{
    "showId": 1,
    "seatIds": [1, 2, 3]
  }'
```

**6. Hold Seats (with discount code)**
```bash
curl -u customer:customer -X POST http://localhost:8080/api/bookings/hold \
  -H "Content-Type: application/json" \
  -d '{
    "showId": 1,
    "seatIds": [4, 5, 6],
    "discountCode": "SAVE20"
  }'
```

**7. Confirm Booking (replace holdId with actual ID from step 5)**
```bash
curl -u customer:customer -X POST http://localhost:8080/api/bookings/confirm \
  -H "Content-Type: application/json" \
  -d '{
    "holdId": 1,
    "paymentMethod": "Credit Card"
  }'
```

**8. View My Bookings**
```bash
curl -u customer:customer http://localhost:8080/api/bookings/my-bookings
```

**9. Cancel Booking (replace bookingId)**
```bash
curl -u customer:customer -X POST http://localhost:8080/api/bookings/1/cancel
```

**10. Validate Discount Code**
```bash
curl -u customer:customer -X POST http://localhost:8080/api/discounts/validate \
  -H "Content-Type: application/json" \
  -d '{
    "code": "SAVE20"
  }'
```

**11. Release Hold Manually (before expiry)**
```bash
curl -u customer:customer -X DELETE http://localhost:8080/api/bookings/hold/1
```

## Sample Data Loaded on Startup

### Cities
- Mumbai (ID: 1)
- Delhi (ID: 2)

### Theaters
- PVR Cinemas Mumbai (ID: 1) - 10 rows, 15 seats per row, rows 1-3 premium
- INOX Delhi (ID: 2) - 10 rows, 15 seats per row, rows 1-3 premium

### Movies
- Inception (ID: 1) - 148 minutes, Sci-Fi
- The Dark Knight (ID: 2) - 152 minutes, Action
- Interstellar (ID: 3) - 169 minutes, Sci-Fi

### Shows
- 4 shows scheduled for tomorrow at 12:00, 15:00, 18:00, 21:00
- Pricing: Regular ₹200, Premium ₹350
- Weekend multiplier: 1.2x on Sat/Sun

### Discount Codes
- **SAVE20** - 20% off, valid for 30 days
- **WEEKEND10** - 10% off, valid for 30 days

## Complete Booking Flow

```bash
# Step 1: Browse shows
curl -u customer:customer http://localhost:8080/api/shows

# Step 2: Get seats for a show (note the show ID)
curl -u customer:customer http://localhost:8080/api/shows/1/seats

# Step 3: Hold seats (note seat IDs that are AVAILABLE)
curl -u customer:customer -X POST http://localhost:8080/api/bookings/hold \
  -H "Content-Type: application/json" \
  -d '{
    "showId": 1,
    "seatIds": [1, 2, 3],
    "discountCode": "SAVE20"
  }'
# Returns: holdId, expiryTime (10 minutes), totalAmount

# Step 4: Confirm booking (within 10 minutes, use holdId from step 3)
curl -u customer:customer -X POST http://localhost:8080/api/bookings/confirm \
  -H "Content-Type: application/json" \
  -d '{
    "holdId": 1,
    "paymentMethod": "Credit Card"
  }'
# Returns: booking details with confirmation

# Step 5: View booking history
curl -u customer:customer http://localhost:8080/api/bookings/my-bookings

# Step 6 (Optional): Cancel booking (use bookingId from step 4)
curl -u customer:customer -X POST http://localhost:8080/api/bookings/1/cancel
# Returns: refund amount based on time until show
```

## Pricing Calculation

**Formula:**
```
basePrice = (category == PREMIUM) ? basePricePremium : basePriceRegular
isWeekend = (showTime is Saturday or Sunday)
weekendPrice = basePrice × (isWeekend ? weekendMultiplier : 1.0)
discountAmount = weekendPrice × (discountPercentage / 100)
finalPrice = weekendPrice - discountAmount
```

**Example:**
- Premium seat: ₹350
- Weekend show: × 1.2 = ₹420
- SAVE20 discount: 20% off = ₹84
- Final price: ₹336

## Refund Policy

Based on time until show:
- **≥24 hours**: 100% refund
- **2-24 hours**: 50% refund
- **<2 hours**: 0% refund

## Concurrency

Multiple customers can attempt to book the same seat:
- First request succeeds
- Second request gets 409 Conflict: "Seat no longer available"
- Handled via optimistic locking (@Version on Seat entity)

## Seat Hold Expiry

Seats held for 10 minutes:
- Automatic release by scheduled job (runs every 60 seconds)
- Manual release available via DELETE endpoint
- Expired holds cannot be confirmed

## Testing Postman Collection

Import these as Postman requests with Basic Auth set to `customer:customer` or `admin:admin`.

## Common HTTP Status Codes

- **200 OK** - Successful GET/PUT
- **201 Created** - Successful POST (resource created)
- **204 No Content** - Successful DELETE
- **400 Bad Request** - Validation error, invalid discount
- **401 Unauthorized** - Missing/invalid credentials
- **403 Forbidden** - Wrong role (customer accessing admin endpoint)
- **404 Not Found** - Resource doesn't exist
- **409 Conflict** - Seat already booked (optimistic lock failure)
- **410 Gone** - Hold expired
- **500 Internal Server Error** - Unexpected error

## Troubleshooting

**"Cannot connect"**
- Ensure app is running: `mvn spring-boot:run`
- Check port 8080 is not in use

**"401 Unauthorized"**
- Verify username/password: admin/admin or customer/customer
- Check HTTP Basic Auth header is included

**"Seat no longer available"**
- Seat was already booked by another user
- Seat hold expired
- Choose different seats

**"Hold expired"**
- More than 10 minutes passed since holding seats
- Create new hold

**"Discount code is not valid"**
- Code doesn't exist, is inactive, or outside valid date range
- Check with validate endpoint first

## Advanced Testing

**Test Concurrent Booking:**
```bash
# Terminal 1
curl -u customer:customer -X POST http://localhost:8080/api/bookings/hold \
  -H "Content-Type: application/json" \
  -d '{"showId": 1, "seatIds": [1]}' &

# Terminal 2 (run immediately)
curl -u customer:customer -X POST http://localhost:8080/api/bookings/hold \
  -H "Content-Type: application/json" \
  -d '{"showId": 1, "seatIds": [1]}'
```
Expected: One succeeds, one fails with 409 Conflict

**Test Hold Expiry:**
```bash
# 1. Hold seats
curl -u customer:customer -X POST http://localhost:8080/api/bookings/hold \
  -H "Content-Type: application/json" \
  -d '{"showId": 1, "seatIds": [10]}'

# 2. Wait 11 minutes (or check scheduler logs)

# 3. Try to confirm expired hold
curl -u customer:customer -X POST http://localhost:8080/api/bookings/confirm \
  -H "Content-Type: application/json" \
  -d '{"holdId": <holdId>, "paymentMethod": "Card"}'
```
Expected: 410 Gone - "Hold has expired"

---

For more details, see the [README.md](README.md) and [Design Specification](docs/superpowers/specs/2026-06-13-movie-ticket-booking-design.md).
