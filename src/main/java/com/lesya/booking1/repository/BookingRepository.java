package com.lesya.booking1.repository;

import com.lesya.booking1.entity.Booking;
import com.lesya.booking1.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

// EN:
// Repository responsible for database operations with Booking entity.
//
// UA:
// Репозиторій, який відповідає за роботу з Booking entity у базі даних.
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // EN:
    // Check whether the selected room already has a booking
    // that overlaps with the requested dates.
    //
    // The overlap condition is: existing.checkIn < newCheckOut  AND  existing.checkOut > newCheckIn
    //
    // UA:
    // Перевірити, чи має вибраний номер уже бронювання,
    // яке перетинається із запитаними датами.
    //
    // Умова перетину: існуючий checkIn < новий checkOut І  існуючий checkOut > новий checkIn
    boolean existsByRoomAndCheckInLessThanAndCheckOutGreaterThan(
            Room room,
            LocalDate checkOut,
            LocalDate checkIn
    );
}