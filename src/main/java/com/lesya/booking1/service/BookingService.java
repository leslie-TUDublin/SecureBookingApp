package com.lesya.booking1.service;

import com.lesya.booking1.dto.booking.BookingRequest;
import com.lesya.booking1.dto.booking.BookingResponse;
import com.lesya.booking1.entity.Booking;
import com.lesya.booking1.entity.BookingStatus;
import com.lesya.booking1.entity.Room;
import com.lesya.booking1.entity.User;
import com.lesya.booking1.exception.ResourceNotFoundException;
import com.lesya.booking1.repository.BookingRepository;
import com.lesya.booking1.repository.RoomRepository;
import com.lesya.booking1.repository.UserRepository;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

// BUSINESS LOGIC OF PROJECT
@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    // Constructor Dependency Injection.
    public BookingService(
            BookingRepository bookingRepository,
            UserRepository userRepository,
            RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }

    // Return all bookings from the database.
    public List<Booking> findAllBookings() {
        return bookingRepository.findAll();
    }

    // Create a new booking for the authenticated user.
    public BookingResponse createBooking(BookingRequest request, String userEmail) {

        // FIND USER by email (розпаковуємо з Optional за допомогою .orElseThrow)
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        // FIND ROOM by its ID (розпаковуємо з Optional за допомогою .orElseThrow)
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + request.getRoomId()));

        // CHECK ROOM AVAILABILITY
        boolean alreadyBooked = bookingRepository
                .existsByRoomAndCheckInLessThanAndCheckOutGreaterThan(
                        room,
                        request.getCheckOut(),
                        request.getCheckIn()
                );

        if (alreadyBooked) {
            throw new IllegalStateException("Room is already booked for these dates!");
        }

        // CALCULATE TOTAL PRICE
        // Calculate the number of nights between check-in and check-out dates.
        long numberOfNights = ChronoUnit.DAYS.between(
                request.getCheckIn(),
                request.getCheckOut()
        );

        // Prevent division/multiplication by 0 nights for same-day trips
        if (numberOfNights <= 0) {
            numberOfNights = 1;
        }

        // Calculate the total booking price:
        BigDecimal totalPrice = room.getPrice()
                .multiply(BigDecimal.valueOf(numberOfNights));

        // CREATE NEW BOOKING ENTITY
        Booking booking = new Booking();
        booking.setCheckIn(request.getCheckIn());
        booking.setCheckOut(request.getCheckOut());
        booking.setUser(user);
        booking.setRoom(room);
        booking.setStatus(BookingStatus.PENDING);
        booking.setTotalPrice(totalPrice);

        // SAVE BOOKING into PostgreSQL.
        Booking savedBooking = bookingRepository.save(booking);

        // RETURN RESPONSE DTO

        return new BookingResponse(
                savedBooking.getId(),
                savedBooking.getRoom().getId(),
                savedBooking.getUser().getEmail(),
                savedBooking.getCheckIn(),
                savedBooking.getCheckOut(),
                savedBooking.getTotalPrice(),
                savedBooking.getStatus()
        );
    }
}