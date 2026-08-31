package com.lesya.booking1.service;


import com.lesya.booking1.dto.booking.BookingRequest;
import com.lesya.booking1.dto.booking.BookingResponse;
import com.lesya.booking1.entity.Booking;
import com.lesya.booking1.entity.BookingStatus;
import com.lesya.booking1.entity.Room;
import com.lesya.booking1.entity.User;
import com.lesya.booking1.exception.ResourceNotFoundException;
import com.lesya.booking1.exception.RoomAlreadyBookedException;
import com.lesya.booking1.exception.InvalidBookingDatesException;
import com.lesya.booking1.repository.BookingRepository;
import com.lesya.booking1.repository.RoomRepository;
import com.lesya.booking1.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

// BUSINESS LOGIC OF PROJECT
@Service
// EN: MAIN business logic.
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;


    public BookingService(
            BookingRepository bookingRepository,
            UserRepository userRepository,
            RoomRepository roomRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
    }


    // Get all bookings
    public List<Booking> findAllBookings() {
        return bookingRepository.findAll();
    }


    // Create new booking
    @Transactional
    // EN: Handles the entire process of booking a room. @Transactional ensures database atomicity.
    // UA: Обробляє весь процес бронювання кімнати. @Transactional гарантує атомарність операцій в БД.
    public BookingResponse createBooking(BookingRequest request, String userEmail) {

        // Find user
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("User not found with email: " + userEmail));

        // Find room
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Room not found with ID: " + request.getRoomId()));

        // Check room availability
        boolean alreadyBooked = bookingRepository
                .existsByRoomAndCheckInLessThanAndCheckOutGreaterThan(
                        room,
                        request.getCheckOut(),
                        request.getCheckIn()
                );

        if (alreadyBooked) {

            throw new RoomAlreadyBookedException
                    ("The selected room is already booked for these dates!");
        }

        // TOTAL PRICE
        long numberOfNights = ChronoUnit.DAYS.between(
                request.getCheckIn(),
                request.getCheckOut()
        );

        //  Check-In >  Check-Out
        if (numberOfNights <= 0) {
            throw new InvalidBookingDatesException
                    ("The check-in date must be earlier than the check-out date. The minimum booking duration is 1 night.");
        }

        // Calculate the total booking price:
        BigDecimal totalPrice = room.getPrice()
                .multiply(BigDecimal.valueOf(numberOfNights));


        // Create new Booking entity
        Booking booking = new Booking();
        booking.setCheckIn(request.getCheckIn());
        booking.setCheckOut(request.getCheckOut());
        booking.setUser(user);
        booking.setRoom(room);
        booking.setStatus(BookingStatus.PENDING);
        booking.setTotalPrice(totalPrice);

        //Save booking
        Booking savedBooking = bookingRepository.save(booking);

        // Return response dto
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