package com.lesya.booking1.dto.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

// EN: DTO used for creating a new booking request. USER sends room ID and check-in/check-out dates.

@Getter
@Setter
@NoArgsConstructor
public class BookingRequest {

    // EN: ID of the room selected by User (cannot be null.
    @NotNull(message = "Room ID cannot be null")
    private Long roomId;

    // EN: Date when USER wants to check in (the date must be provided вказана).
    @NotNull(message = "Check-in date cannot be null")
    @Future(message = "Check-in date must be in the future")
    private LocalDate checkIn;

    // EN: Date when USER wants to check out.(date must be provided).
    @NotNull(message = "Check-out date cannot be null")
    @Future(message = "Check-out date must be in the future")
    private LocalDate checkOut;
}
