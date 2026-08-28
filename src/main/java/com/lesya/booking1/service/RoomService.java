package com.lesya.booking1.service;

import com.lesya.booking1.entity.Room;
import com.lesya.booking1.repository.RoomRepository;
import com.lesya.booking1.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
// Hey Spring, Register this class as a Spring Service Bean. This class contains the business logic related to hotel rooms.

public class RoomService {

    // EN: Repository used to communicate with the "rooms" database table.
    private final RoomRepository roomRepository;


    // EN: Constructor Dependency Injection.
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // CREATE ROOM
    //  Create and save a new hotel room in the database.
    public Room createRoom(Room room) {

        // EN: Save the Room entity using Spring Data JPA. PostgreSQL will generate the ID automatically.
        return roomRepository.save(room);
    }

    // GET ALL ROOMS
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // GET AVAILABLE ROOMS
    // Return only rooms that are currently marked as available.
    public List<Room> getAvailableRooms() {

        //  Call the custom repository method
        return roomRepository.findByAvailableTrue();
    }

    // GET ROOM BY ID
    public Optional<Room> getRoomById(Long id) {

        // JpaRepository already provides findById().
        // Optional is returned because the room may not exist.
        return roomRepository.findById(id);
    }

    // GET ROOM BY NUMBER
    public Optional<Room> getRoomByNumber(String number) {
        return roomRepository.findByNumber(number);
    }

    // UPDATE ROOM
    public Room updateRoom(Long id, Room updatedRoom) {

        // First find the existing room in the database.
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + id));

        // Update room number.
        existingRoom.setNumber(updatedRoom.getNumber());

        // Update room type.
        existingRoom.setType(updatedRoom.getType());

        // Update price per night.
        existingRoom.setPrice(updatedRoom.getPrice());

        // Update maximum number of guests.
        existingRoom.setCapacity(updatedRoom.getCapacity());

        // Update room availability.
        existingRoom.setAvailable(updatedRoom.getAvailable());

        // Save updated room back to the database.
        return roomRepository.save(existingRoom);
    }


    // DELETE ROOM by its ID.
    public void deleteRoom(Long id) {

        roomRepository.deleteById(id);
    }
}