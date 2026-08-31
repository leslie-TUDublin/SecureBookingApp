package com.lesya.booking1.repository;

import com.lesya.booking1.entity.Room;
import com.lesya.booking1.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;

public interface BookingRepository extends JpaRepository <Booking, Long>{

    // Automatically generate SQL query to check if the selected room has the booking that owerlaps with rhe existing bookings
    //Owerlap Conditions: existing_checkIn < new_checkOut  and (AND!!!!!)  existing_checkOut > new_checkIn
    boolean existsByRoomAndCheckInLessThanAndCheckOutGreaterThan(
            Room room,
            LocalDate checkOut,
            LocalDate checkIn
    );

}
