package com.lesya.booking1.repository;

import com.lesya.booking1.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    // EN: Find a room by its unique room number.
    // Example: Search room "101" in database.
    //
    // UA: Знайти кімнату за її унікальним номером.
    // Приклад: Знайти кімнату "101" у базі даних.
    Optional<Room> findByNumber(String number);

    // EN: Find all available rooms.
    // UA: Знайти всі доступні кімнати.
    // Виправлено: Змінено Optional на List, щоб повертати список усіх вільних кімнат.
    List<Room> findByAvailableTrue();
}
