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

import java.util.List;

// BUSINESS LOGIC OF  PROJECT
@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    //  Constructor Dependency Injection.
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

    //  Create a new booking for the authenticated user.

    public BookingResponse createBooking(
            BookingRequest request,
            String userEmail) {

        //  FIND USER  by email.
        User user = userRepository.findByEmail(userEmail);


        //  FIND ROOM  by its ID.
        Room room = roomRepository.findById(request.getRoomId());



        //  CHECK ROOM AVAILABILITY

        boolean alreadyBooked =
                bookingRepository
                        .existsByRoomAndCheckInLessThanAndCheckOutGreaterThan(
                                room,
                                request.getCheckOut(),
                                request.getCheckIn()
                        );


        // CALCULATE TOTAL PRICE
        // Calculate the number of nights between check-in and check-out dates.
        long numberOfNights = ChronoUnit.DAYS.between(
                request.getCheckIn(),
                request.getCheckOut()
        );

        //  Calculate the total booking price:
        BigDecimal totalPrice = room.getPrice()
                .multiply(BigDecimal.valueOf(numberOfNights));

        //  CREATE NEW BOOKING ENTITY

        Booking booking = new Booking();

        // Copy check-in date from DTO into entity.
        booking.setCheckIn(request.getCheckIn());

        // Copy check-out date from DTO into entity.
        booking.setCheckOut(request.getCheckOut());

        // Connect the booking with the authenticated user.
        booking.setUser(user);

        // Connect the booking with the selected room.
        booking.setRoom(room);

        // Set the initial booking status.
        booking.setStatus(BookingStatus.PENDING);

        // Set the total price
        booking.setTotalPrice(totalPrice);

        //  SAVE BOOKING into PostgreSQL.
        Booking savedBooking = bookingRepository.save(booking);

        // RETURN RESPONSE DTO
        // Convert the saved entity into a response DTO.

        return new BookingResponse(
                savedBooking.getId(),
                savedBooking.getRoom().getId(),
                savedBooking.getCheckIn(),
                savedBooking.getCheckOut(),
                savedBooking.getUser().getEmail()
        );
    }
}