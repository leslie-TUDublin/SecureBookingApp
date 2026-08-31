package com.lesya.booking1.service;

import com.lesya.booking1.entity.Room;
import com.lesya.booking1.repository.RoomRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
// EN: Registers this class as a Spring Service Bean containing hotel rooms business logic.
// UA: Реєструє цей клас як Spring Service Bean, що містить бізнес-логіку готельних номерів.
public class RoomService {

    // EN: Repository used to communicate with the "rooms" database table.
    // UA: Репозиторій для зв'язку з таблицею "rooms" у базі даних.
    private final RoomRepository roomRepository;

    // EN: Constructor Dependency Injection.
    // UA: Впровадження залежності через конструктор.
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // CREATE ROOM
    // EN: Create and save a new hotel room in the database.
    // UA: Створити та зберегти новий готельний номер у базі даних.
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    // GET ALL ROOMS
    // EN: Retrieve all rooms from the database.
    // UA: Отримати всі номери з бази даних.
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // GET AVAILABLE ROOMS
    // EN: Return only rooms that are currently marked as available.
    // UA: Повернути тільки ті номери, які зараз позначені як доступні.
    public List<Room> getAvailableRooms() {
        return roomRepository.findByAvailableTrue();
    }

    // GET ROOM BY ID
    // EN: Find a specific room by its ID. Optional is used because the room may not exist.
    // UA: Знайти конкретний номер за його ID. Optional використовується, бо номер може бути відсутній.
    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    // GET ROOM BY NUMBER
    // EN: Find a room by its unique room number.
    // UA: Знайти номер за його унікальним номером кімнати.
    public Optional<Room> getRoomByNumber(String number) {
        return roomRepository.findByNumber(number);
    }

    // UPDATE ROOM
    // EN: Find an existing room, update its fields, and save changes.
    // UA: Знайти існуючий номер, оновити його поля та зберегти зміни.
    public Room updateRoom(Long id, Room updatedRoom) {
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new com.lesya.booking1.exception.ResourceNotFoundException("Room not found with ID: " + id));

        existingRoom.setNumber(updatedRoom.getNumber());
        existingRoom.setType(updatedRoom.getType());
        existingRoom.setPrice(updatedRoom.getPrice());
        existingRoom.setCapacity(updatedRoom.getCapacity());
        existingRoom.setAvailable(updatedRoom.getAvailable());

        return roomRepository.save(existingRoom);
    }

    // DELETE ROOM
    // EN: Delete a room from the database by its ID.
    // UA: Видалити номер з бази даних за його ID.
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
}