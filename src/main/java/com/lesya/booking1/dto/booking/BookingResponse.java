package com.lesya.booking1.dto.booking;

import com.lesya.booking1.entity.BookingStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

// EN: DTO returned to USER after successful booking creation(contains information about the created booking,
// but does not expose the entire Booking entity).
@Getter
public class BookingResponse {
    // EN: Unique identifier of the booking.
    private final Long id;

    // EN: ID of the booked room.
    private final Long roomId;

    // EN: Email of USER who created the booking (The email comes from the authenticated user).
    // Email користувача, який створив бронювання. Email береться з авторизованого користувача.
    private final String userEmail;

    // EN: Date when USER checks in.
    private final LocalDate checkIn;

    // EN: Date when USER checks out.
    private final LocalDate checkOut;


    // EN: Total price of the entire booking. BigDecimal is used because this value represents money.
    private final BigDecimal totalPrice;


    // EN: Current status of the booking. For a newly created booking this will normally be PENDING.
    private final BookingStatus status;

    public BookingResponse(
            Long id,
            Long roomId,
            String userEmail,
            LocalDate checkIn,
            LocalDate checkOut,
            BigDecimal totalPrice,
            BookingStatus status
    ) {
        this.id = id;
        this.roomId = roomId;
        this.userEmail = userEmail;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalPrice = totalPrice;
        this.status = status;
    }
}

