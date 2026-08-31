package com.lesya.booking1.controller;

import com.lesya.booking1.dto.booking.BookingRequest;
import com.lesya.booking1.dto.booking.BookingResponse;
import com.lesya.booking1.entity.Booking;
import com.lesya.booking1.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Endpoint to fetch all bookings from the database (FOR ADMIN)
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.findAllBookings();
        return ResponseEntity.ok(bookings);
    }

    // Endpoint to create a new booking for Currently Authenticated user.
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            //@Valid activates DTO-validation and MethodArgumentNotValidException
            @Valid @RequestBody BookingRequest request,
            // principal fetch (safely!!!) USER CLAIMS(JWT Claims) from SecurityContext (JWT)
            Principal principal
    ) {
        // Extract logged-in user's email from the security Principal.
        String userEmail = principal.getName();

        // Execute MAIN business logic and return mapped response with 201 Created status.
        BookingResponse response = bookingService.createBooking(request, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}