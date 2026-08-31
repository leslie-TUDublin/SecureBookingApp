package com.lesya.booking1.controller;

import com.lesya.booking1.entity.Room;
import com.lesya.booking1.exception.ResourceNotFoundException;
import com.lesya.booking1.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
// REST Controller for managing hotel rooms.
public class RoomController {

    private final RoomService roomService;
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }


    // Endpoint to fetch all hotel rooms.
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }


    // Endpoint to fetch only rooms that are currently free/available.
    @GetMapping("/available")
    public ResponseEntity<List<Room>> getAvailableRooms() {
        List<Room> rooms = roomService.getAvailableRooms();
        return ResponseEntity.ok(rooms);
    }


    // Endpoint to fetch a single room details.
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        // EN: Unwrap Optional using Custom Exception for a  HTTP 404 response.
        Room room = roomService.getRoomById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + id));
        return ResponseEntity.ok(room);
    }


    // Endpoint to add a new room to the hotel catalog.(JUST for ADMIN)
    @PostMapping
    public ResponseEntity<Room> createRoom(@Valid @RequestBody Room room) {
        Room savedRoom = roomService.createRoom(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
    }

    // Endpoint to update an existing room's details.(JUST for ADMIN)
    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @Valid @RequestBody Room room) {
        Room updatedRoom = roomService.updateRoom(id, room);
        return ResponseEntity.ok(updatedRoom);
    }

    // Endpoint to absolutely remove a room from the database.
        @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build(); // HTTP 204 No Content
    }
}
