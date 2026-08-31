package com.lesya.booking1.repository;

import com.lesya.booking1.entity.Room;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoomRepository extends JpaRepository <Room, Long> {

    //Automatically generate SQL query to select the room inside database by rooms uniq number
    Optional <Room> findByNumber (String number);

    //Automatically generate SQL query to select  all available rooms
    List <Room> findByAvailableTrue();

}
