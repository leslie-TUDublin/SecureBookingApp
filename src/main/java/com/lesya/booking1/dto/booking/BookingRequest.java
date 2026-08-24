package com.lesya.booking1.dto.booking;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor

public class BookingRequest {
    @NotNull
    // EN: ID of the room selected by the user.
    private Long roomId;

    @NotNull
    // EN: Date when the guest checks in.
    // UA: Дата заселення гостя.
    private LocalDate checkIn;

    @NotNull
    // EN: Date when the guest checks out.
    // UA: Дата виселення гостя.
    private LocalDate checkOut;
}
