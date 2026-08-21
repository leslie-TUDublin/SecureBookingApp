package com.lesya.booking1.repository;

import com.lesya.booking1.entity.Booking;
import com.lesya.booking1.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

// EN:
// Repository responsible for database operations with Booking entity.
//
// UA:
// Repository, який відповідає за роботу з Booking entity у базі даних.
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // EN:
    // Check if a room already has a booking that overlaps
    // with requested dates.
    //
    // Existing booking:
    //
    // existing.start < requested.end
    // AND
    // existing.end > requested.start
    //
    // UA:
    // Перевірити чи має номер вже існуюче бронювання,
    // яке перетинається з новими датами.
    //
    // Логіка:
    //
    // існуючий початок < новий кінець
    // І
    // існуючий кінець > новий початок
    boolean existsByRoomAndStartTimeLessThanAndEndTimeGreaterThan(
            Room room,
            LocalDateTime endTime,
            LocalDateTime startTime
    );
}
