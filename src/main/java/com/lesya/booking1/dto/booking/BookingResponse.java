package com.lesya.booking1.dto.booking;

import lombok.Getter;

import java.time.LocalDate;

// EN: Response DTO returned to the client after successful booking creation.
// UA: DTO відповіді, який повертається клієнту після успішного створення бронювання.
@Getter
public class BookingResponse {

    // EN: Unique identifier of the booking.
    // UA: Унікальний ідентифікатор бронювання.
    private final Long id;

    // EN: ID of the booked room.
    // UA: ID заброньованого номера.
    private final Long roomId;

    // EN: Date when the guest checks in.
    // UA: Дата заселення гостя.
    private final LocalDate checkIn;

    // EN: Date when the guest checks out.
    // UA: Дата виселення гостя.
    private final LocalDate checkOut;

    // EN: Email of the user who created the booking.
    // UA: Email користувача, який створив бронювання.
    private final String userEmail;

    public BookingResponse(
            Long id,
            Long roomId,
            LocalDate checkIn,
            LocalDate checkOut,
            String userEmail) {

        this.id = id;
        this.roomId = roomId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.userEmail = userEmail;
    }
}


